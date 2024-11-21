package com.example.face_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.face_test.ml.DefiningEmotions0504;
import com.example.face_test.ml.Model;
import com.example.face_test.ml.Save;

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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    JavaCameraView javaCameraView;
    File caseFile;
    CascadeClassifier faceDetector;
    private Mat mRgba, mGrey;
    private Bitmap bitmap;
    private TextView textViewResult;
    private ImageView imageView;
    private ProgressBar progressBar;
    private int angle, iterCount = 0;
    private String mostFrequentEmotion = "";
    private String[] emotionLabels1 = {"surprise", "fear", "angry", "neutral", "sad", "disgust", "happy"};
    private String[] classLabels = {"Злость", "Неприязнь", "Страх", "Счастье", "Спокойствие", "Грусть", "Удивление"};
    private String[] emotionLabels = {"Удивление", "Страх", "Злость", "Спокойствие", "Грусть", "Неприязнь", "Счастье"};
    private String[] finalEmotions = new String[emotionLabels.length];
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
    private HashMap<String, Integer> emotionsCount = new HashMap<>();
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
            angle+=5;
            progressBar.setProgress(angle);
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
                emotionsCount.put(classLabels[predictedIndex], emotionsCount.getOrDefault(classLabels[predictedIndex], 0) + 1);
                model.close();
                if(angle==100){
                    int maxCount = 0;
                    for (String emotion : emotionsCount.keySet()) {
                        int count = emotionsCount.get(emotion);
                        if (count > maxCount) {
                            maxCount = count;
                            mostFrequentEmotion = emotion;
                        }
                    }
                    finalEmotions[iterCount] = mostFrequentEmotion;
                    Log.d("Emotion", finalEmotions[iterCount]);
                    iterCount+=1;
                    if(iterCount==7) {
                        Intent intent = new Intent(this, MainEmotionRecognitionResult.class);
                        intent.putExtra("finalEmotions",finalEmotions);
                        intent.putExtra("emotions",emotionLabels);
                        startActivity(intent);
                        break;
                    }
                    angle=0;
                    mostFrequentEmotion="";
                    emotionsCount.clear();
                    int finalIterCount = iterCount;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageResource(getResources().getIdentifier(emotionLabels1[finalIterCount]+"_emotion", "drawable", getPackageName()));
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            roi.release();
        }
        return mRgba;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView) findViewById(R.id.javaCameraView);
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        javaCameraView.setCameraIndex(1);
        progressBar = (ProgressBar) findViewById(R.id.my_progressBar);
        imageView = findViewById(R.id.loadingImageView);
        textViewResult=findViewById(R.id.resultText);
        imageView.setImageResource(getResources().getIdentifier(emotionLabels1[0]+"_emotion", "drawable", getPackageName()));
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, baseCallback);
        } else {
            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        javaCameraView.setCvCameraViewListener(this);
    }
}