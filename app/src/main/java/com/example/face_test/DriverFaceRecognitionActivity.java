package com.example.face_test;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.face_test.ml.DefiningEmotions0504;
import com.example.face_test.ml.Model;
import com.example.face_test.ml.Save;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DriverFaceRecognitionActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, TextToSpeech.OnInitListener {
    private JavaCameraView javaCameraView;
    private File caseFile;
    private CascadeClassifier faceDetector;
    private Mat mRgba, mGrey;
    private Bitmap bitmap;
    private TextView textViewResult;
    private Button button_close;
    private TextToSpeech tts;
    private String phrase;
    private int randomNum;
    private boolean phraseIsEmpty=false;
    private int negativeEmotions=0;
    private FirebaseAuth mAuth;
    private String phraseList[]={
            "Все в порядке. Остаться спокойным поможет концентрация на дороге.",
            "Дышите глубоко и медленно. Сосредоточьтесь на безопасном вождении.",
            "Успокойтесь. Профессиональные водители могут решать сложные ситуации.",
            "Не позволяйте эмоциям управлять вашими действиями. Оставайтесь спокойными и внимательными.",
            "Ваше безопасное прибытие важнее всего. Оставайтесь спокойными и осторожными на дороге.",
            "Не дайте эмоциям взять верх. Оставайтесь фокусированными и расслабленными."
    };
    private String[] classLabels = {"Злость", "Неприязнь", "Страх", "Счастье", "Спокойствие", "Грусть", "Удивление"};
    BaseLoaderCallback baseCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    caseFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(caseFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while (true) {
                        try {
                            if ((bytesRead = is.read(buffer)) == -1) break;
                            fos.write(buffer, 0, bytesRead);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        is.close();
                        fos.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    faceDetector = new CascadeClassifier(caseFile.getAbsolutePath());
                    if (faceDetector.empty()) {
                        faceDetector = null;
                    } else {
                        cascadeDir.delete();
                    }
                    javaCameraView.enableView();
                }
                break;

                default:
                    super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGrey = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGrey.release();
    }
    private static Bitmap convertMatToBitMap(Mat input){
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_GRAY2BGR);

        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        }
        catch (CvException e){

        }
        return bmp;
    }
    private int getMaxIndex(float[] array) {
        int maxIndex = 0;
        float maxVal = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxVal = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGrey = inputFrame.gray();
        Mat rotImage = Imgproc.getRotationMatrix2D(new Point(mRgba.cols() / 2,
                mRgba.rows() / 2), 90, 1);
        Mat rotGrayImage = Imgproc.getRotationMatrix2D(new Point(mGrey.cols() / 2,
                mGrey.rows() / 2), 90, 1);
        Imgproc.warpAffine(mRgba, mRgba, rotImage, mRgba.size());
        Imgproc.warpAffine(mGrey, mGrey, rotGrayImage, mGrey.size());
        MatOfRect facedetections = new MatOfRect();
        faceDetector.detectMultiScale(mGrey, facedetections);
        for (Rect rect : facedetections.toArray()) {
            Imgproc.rectangle(mRgba, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
            int x = rect.x;
            int y = rect.y;
            int width = rect.width;
            int height = rect.height;
            Mat roi = new Mat(mGrey, new Rect(x, y, width, height));
            Imgproc.resize(roi, roi, new Size(71, 71));
            bitmap = Bitmap.createBitmap(roi.cols(), roi.rows(), Bitmap.Config.ARGB_8888);
            bitmap=convertMatToBitMap(roi);
            try {
                TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                tensorImage.load(bitmap);
                ByteBuffer byteBuffer = tensorImage.getBuffer();
                Save model = Save.newInstance(getApplicationContext());
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 71, 71, 3}, DataType.FLOAT32);
                inputFeature0.loadBuffer(byteBuffer);
                Save.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                int predictedIndex = getMaxIndex(outputFeature0.getFloatArray());
                textViewResult.setText(classLabels[predictedIndex]);
                if(isInScale(classLabels[predictedIndex], "Злость", "Неприязнь", "Страх", "Грусть")){
                    negativeEmotions+=1;
                }
                if(negativeEmotions>=20){
                    speakOut();
                    negativeEmotions=0;
                }
                model.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            roi.release();
        }
        return mRgba;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = new Locale("RU");
            int result = tts.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
                String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("phrase_when_driving");
                userRoleRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            phrase = dataSnapshot.getValue(String.class);
                        }
                        else{
                            phraseIsEmpty=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                    }
                });
            }
            else if (TextUtils.isEmpty(phrase)) {
                phraseIsEmpty=true;
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    private void speakOut() {
        if (phraseIsEmpty) {
            randomNum = getRandomNumberUsingNextInt(0,6);
            phrase = phraseList[randomNum];
        }
        tts.speak(phrase, TextToSpeech.QUEUE_FLUSH, null);

    }
    private boolean isInScale(String scaleIndex, String ... items) {
        for (String item : items) {
            if (item == scaleIndex) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_driver_face_recognition);
        mAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            phrase = extras.getString("phrase");
        tts = new TextToSpeech(this, this);
        javaCameraView = (JavaCameraView) findViewById(R.id.javaCameraView);
        button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tts != null) {
                    tts.stop();
                    tts.shutdown();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                intent.putExtra("selectedTab", R.id.navigation_dashboard);
                startActivity(intent);
            }
        });
        javaCameraView.setCameraIndex(1);
        textViewResult=findViewById(R.id.resultText);
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, baseCallback);
        } else {
            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        javaCameraView.setCvCameraViewListener(this);
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}