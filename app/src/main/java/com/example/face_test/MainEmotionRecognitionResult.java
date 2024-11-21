package com.example.face_test;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.face_test.ui.notifications.EmotionData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainEmotionRecognitionResult extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_emotion_recognition_result);
        Button button_close = findViewById(R.id.button_close);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        String [] finalEmotions = getIntent().getStringArrayExtra("finalEmotions");
        String [] emotionsLabel = getIntent().getStringArrayExtra("emotions");
        button_close.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_dashboard);
            startActivity(intent);
        });
        for (int i = 0; i < finalEmotions.length; i++) {
            TableRow row = new TableRow(MainEmotionRecognitionResult.this);
            TextView emotionsTextView = new TextView(this);
            emotionsTextView.setText(emotionsLabel[i]);
            TextView finalEmotionsTextView = new TextView(this);
            finalEmotionsTextView.setText(String.valueOf(finalEmotions[i]));
            emotionsTextView.setGravity(Gravity.CENTER);
            finalEmotionsTextView.setGravity(Gravity.CENTER);
            row.addView(emotionsTextView);
            row.addView(finalEmotionsTextView);
            tableLayout.addView(row);
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String uid = mAuth.getCurrentUser().getUid();
            String currentTime = String.valueOf(System.currentTimeMillis());
            String path = "users_emotions/" + uid + "/faceRecognition/"+currentTime;
            List<String> emotionsList = new ArrayList<>();
            for(int i=0;i<finalEmotions.length;i++){
                emotionsList.add(finalEmotions[i]);
            }
            Map<String, Object> emotionsMap = new HashMap<>();
            emotionsMap.put(currentTime, emotionsList);
            DatabaseReference userEmotionsRef = database.getReference(path);
            userEmotionsRef.setValue(emotionsList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Данные успешно сохранены в базе данных Firebase.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Ошибка при сохранении данных в базе данных Firebase.", e);
                        }
                    });
        }
    }
}