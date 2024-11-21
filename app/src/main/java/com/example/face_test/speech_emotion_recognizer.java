//package com.example.face_test;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.os.Bundle;
//import android.os.Environment;
//import android.widget.Button;
//import android.widget.TextView;
//import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.example.face_test.ml.SpeechRecognitionModel;
//
//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.List;
//
//import be.tarsos.dsp.AudioDispatcher;
//import be.tarsos.dsp.AudioEvent;
//import be.tarsos.dsp.AudioProcessor;
//import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
//import be.tarsos.dsp.io.android.AudioDispatcherFactory;
//import be.tarsos.dsp.mfcc.MFCC;
//
//public class speech_emotion_recognizer extends AppCompatActivity {
//
//
//    private static final int threshold = 500;
//    private static final int CHUNK_SIZE = 1024;
//    private static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int RATE = 16000;
//    private static final int SILENCE = 30;
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//    private static final String TAG = "MainActivity";
//
//    private static String fileName = null;
//    private boolean permissionToRecordAccepted = false;
//    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
//
//    private AudioRecord audioRecord;
//    private int bufferSize;
//    private boolean isRecording = false;
//    private Thread recordingThread;
//    TextView textView_result;
//    private MediaRecorder recorder = null;
//
////    public boolean isSilent(short[] audioData) {
////        for (short sample : audioData) {
////            if (sample >= threshold) {
////                return false;
////            }
////        }
////        return true;
////    }
////
////    public short[] normalize(short[] audioData) {
////        final int MAXIMUM = 16384;
////        int maxAbsValue = 0;
////        for (short value : audioData) {
////            int absValue = Math.abs(value);
////            if (absValue > maxAbsValue) {
////                maxAbsValue = absValue;
////            }
////        }
////        double times = (double) MAXIMUM / maxAbsValue;
////
////        short[] normalizedData = new short[audioData.length];
////        for (int i = 0; i < audioData.length; i++) {
////            normalizedData[i] = (short) (audioData[i] * times);
////        }
////
////        return normalizedData;
////    }
////
////    public short[] trim(short[] audioData, int threshold) {
////        List<Short> trimmedData = new ArrayList<>();
////
////        boolean sndStarted = false;
////        for (short sample : audioData) {
////            if (!sndStarted && Math.abs(sample) > threshold) {
////                sndStarted = true;
////                trimmedData.add(sample);
////            } else if (sndStarted) {
////                trimmedData.add(sample);
////            }
////        }
////
////        Collections.reverse(trimmedData);
////
////        sndStarted = false;
////        List<Short> finalTrimmedData = new ArrayList<>();
////        for (short sample : trimmedData) {
////            if (!sndStarted && Math.abs(sample) > threshold) {
////                sndStarted = true;
////                finalTrimmedData.add(sample);
////            } else if (sndStarted) {
////                finalTrimmedData.add(sample);
////            }
////        }
////
////        Collections.reverse(finalTrimmedData);
////
////        short[] result = new short[finalTrimmedData.size()];
////        for (int i = 0; i < finalTrimmedData.size(); i++) {
////            result[i] = finalTrimmedData.get(i);
////        }
////
////        return result;
////    }
////
////    public short[] addSilence(short[] audioData, double seconds, int sampleRate) {
////        int numSamples = (int) (seconds * sampleRate);
////        short[] silence = new short[numSamples];
////        Arrays.fill(silence, (short) 0);
////
////        short[] result = new short[audioData.length + 2 * numSamples];
////        System.arraycopy(silence, 0, result, 0, numSamples); // Добавляем тишину в начало
////        System.arraycopy(audioData, 0, result, numSamples, audioData.length); // Копируем исходные данные
////        System.arraycopy(silence, 0, result, numSamples + audioData.length, numSamples); // Добавляем тишину в конец
////
////        return result;
////    }
////    public static double[] extractMFCC(String filePath) {
////        List<Double> mfccValues = new ArrayList<>();
////        try {
////            AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(filePath, 44100, 1024, 0);
////            MFCC mfcc = new MFCC(1024, 44100, 13, 40, 300, 3000);
////
////            dispatcher.addAudioProcessor(mfcc);
////
////            dispatcher.addAudioProcessor(new AudioProcessor() {
////                @Override
////                public boolean process(AudioEvent audioEvent) {
////                    float[] mfccBuffer = mfcc.getMFCC();
////                    for (float value : mfccBuffer) {
////                        mfccValues.add((double) value);
////                    }
////                    return true;
////                }
////
////                @Override
////                public void processingFinished() {
////                    // Можно добавить что-то, что нужно выполнить после обработки аудио
////                }
////            });
////
////            dispatcher.run();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        double[] mfccArray = new double[mfccValues.size()];
////        for (int i = 0; i < mfccValues.size(); i++) {
////            mfccArray[i] = mfccValues.get(i);
////        }
////
////        return mfccArray;
////    }
//public static float[] extractFeatures(String audioFilePath) {
//    List<Float> features = new ArrayList<>();
//    AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(audioFilePath, 44100, 1024, 0);
//    MFCC mfcc = new MFCC(1024, 44100, 13, 40, 300, 3000);
//
//    dispatcher.addAudioProcessor(mfcc);
//
//    dispatcher.addAudioProcessor(new AudioProcessor() {
//        @Override
//        public boolean process(AudioEvent audioEvent) {
//            float[] mfccBuffer = mfcc.getMFCC();
//            for (float value : mfccBuffer) {
//                features.add(value);
//            }
//            return true;
//        }
//
//        @Override
//        public void processingFinished() {
//            // Можно добавить что-то, что нужно выполнить после обработки аудио
//        }
//    });
//
//    dispatcher.run();
//
//    // Преобразование в массив float
//    float[] featureArray = new float[features.size()];
//    for (int i = 0; i < features.size(); i++) {
//        featureArray[i] = features.get(i);
//    }
//
//    return featureArray;
//}
//    private int getMaxIndex(int[] array) {
//        int maxIndex = 0;
//        int maxVal = array[0];
//        for (int i = 1; i < array.length; i++) {
//            if (array[i] > maxVal) {
//                maxVal = array[i];
//                maxIndex = i;
//            }
//        }
//        return maxVal;
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        fileName = getExternalCacheDir().getAbsolutePath() + "/audio_recording.wav";
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
//        setContentView(R.layout.activity_speech_emotion_recognizer);
//        Button continue_btn = findViewById(R.id.continue_btn);
//        textView_result = findViewById(R.id.textView_result);
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
//        bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT);
//        startRecording();
//
//        // Initialize AndroidFFMPEGLocator
//
//        continue_btn.setOnClickListener(v -> {
//            stopRecording();
//            try {
//                String[] classLabels = {"anger", "disgust", "enthusiasm", "fear", "happiness", "neutral", "sadness"};
//                String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_recording.wav";
//
//                // Initialize AndroidFFMPEGLocator before calling extractFeatures
//                AndroidFFMPEGLocator locator = new AndroidFFMPEGLocator(this);
//
//                float[] audioFeatures = extractFeatures(audioFilePath);
//                File audioFile = new File(audioFilePath);
//                int fileSize = (int) audioFile.length();
//                SpeechRecognitionModel model = SpeechRecognitionModel.newInstance(getApplicationContext());
//                // Creates inputs for reference.
//                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 180}, DataType.FLOAT32);
//                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(fileSize);
//                for (int i = 0; i < audioFeatures.length; i++) {
//                    byteBuffer.putFloat((float) audioFeatures[i]);
//                }
//                inputFeature0.loadBuffer(byteBuffer);
//
//                // Runs model inference and gets result.
//                SpeechRecognitionModel.Outputs outputs = model.process(inputFeature0);
//                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                int predictedIndex = getMaxIndex(outputFeature0.getIntArray());
//                textView_result.setText(classLabels[predictedIndex]);
//                // Releases model resources if no longer used.
//                model.close();
//
//            } catch (Exception e) {
//                // Обработка ошибок
//                e.printStackTrace();
//            }
////            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_recording.wav";
////            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
////            startActivity(intent);
//        });
//    }
//
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        switch (requestCode) {
////            case REQUEST_RECORD_AUDIO_PERMISSION:
////                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
////                if (!permissionToRecordAccepted) finish();
////
////                break;
////        }
////    }
//
////    private void startRecording() {
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return;
////        }
////        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
////                44100, AudioFormat.CHANNEL_IN_MONO,
////                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
////
////        audioRecord.startRecording();
////        isRecording = true;
////
////        recordingThread = new Thread(new Runnable() {
////            public void run() {
////                writeAudioDataToFile();
////            }
////        }, "AudioRecorder Thread");
////        recordingThread.start();
////    }
//    private void startRecording() {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setOutputFile(fileName);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            recorder.prepare();
//        } catch (IOException e) {
//        }
//
//        recorder.start();
//    }
//
//    private void stopRecording() {
//        recorder.stop();
//        recorder.release();
//        recorder = null;
//    }
//
////    private void writeAudioDataToFile() {
////        byte data[] = new byte[bufferSize];
////        FileOutputStream os = null;
////
////        try {
////            os = new FileOutputStream(filePath);
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////
////        if (null != os) {
////            while (isRecording) {
////                int read = audioRecord.read(data, 0, bufferSize);
////                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
////                    try {
////                        os.write(data);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////
////            try {
////                os.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////    }
//
////    private void stopRecording() {
////        if (null != audioRecord) {
////            isRecording = false;
////            audioRecord.stop();
////            audioRecord.release();
////            audioRecord = null;
////            recordingThread = null;
////        }
////    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopRecording();
//    }
//}

package com.example.face_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.face_test.ml.SpeechRecognitionModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;
public class speech_emotion_recognizer extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String TAG = "MainActivity";

    private static String fileName = null;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private MediaRecorder recorder = null;
    private TextView textView_result;
    private MediaPlayer player = null;
    private boolean recordingAudio = false;
    private void startRecording() {
        recordingAudio=true;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            // Handle recording preparation error
            e.printStackTrace();
        }

        recorder.start();
    }

    private void stopRecording() {
        recordingAudio=false;
        recorder.stop();
        recorder.release();
        recorder = null;
    }
    private ByteBuffer readAudioFile(String filePath) {
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ByteBuffer.wrap(bytes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileName = getExternalCacheDir().getAbsolutePath() + "/audio_recording.wav";
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_speech_emotion_recognizer);
        Button continue_btn = findViewById(R.id.continue_btn);
        Button button_close = findViewById(R.id.button_close);
        textView_result = findViewById(R.id.textView_result);
        startRecording();
        button_close.setOnClickListener(v -> {
            if(recordingAudio){
                stopRecording();
            }
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("navigate_to_dashboard", true);
            startActivity(intent);
        });
        continue_btn.setOnClickListener(v -> {
            stopRecording();
            String[] classLabels = {"anger", "disgust", "enthusiasm", "fear", "happiness", "neutral", "sadness"};

            try {
                //fileName = "android.resource://" + getPackageName() + "/" + R.raw.clouds;;
                // Extract features from audio file
                player = new MediaPlayer();
                player.setDataSource(fileName);
                player.prepare();
                player.start();
                // Convert features to ByteBuffer
//                ByteBuffer byteBuffer = readAudioFile(fileName);

                // Load and use the pre-trained SpeechRecognitionModel
//                SpeechRecognitionModel model = SpeechRecognitionModel.newInstance(getApplicationContext());
//                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 180}, DataType.FLOAT32);
//                inputFeature0.loadBuffer(byteBuffer);

                // Run model inference and get the result
//                SpeechRecognitionModel.Outputs outputs = model.process(inputFeature0);
//                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                int predictedIndex = getMaxIndex(outputFeature0.getIntArray());
//                textView_result.setText(classLabels[predictedIndex]);

                // Close the model to release resources
//                model.close();
            } catch (IOException e) {
                // Handle IOException
                e.printStackTrace();
            }
        });
    }

    private int getMaxIndex(int[] array) {
        int maxIndex = 0;
        int maxVal = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxVal = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!permissionToRecordAccepted) finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}



