package com.example.face_test.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.face_test.AnxietiesActivity;
import com.example.face_test.CardsActivity;
import com.example.face_test.CogniTestActivity;
import com.example.face_test.DriverFaceRecognitionActivity;
import com.example.face_test.MainActivity;
import com.example.face_test.R;
import com.example.face_test.TestActivity;
import com.example.face_test.databinding.FragmentDashboardBinding;
import com.example.face_test.speech_emotion_recognizer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

private FragmentDashboardBinding binding;
    private FirebaseAuth mAuth;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

    binding = FragmentDashboardBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        Button face_recognition_btn, speech_recognition_btn, cards_btn, anxiety_btn, test_btn, cogni_btn, driver_face_recognition_btn;
        face_recognition_btn = root.findViewById(R.id.face_recognition_btn);
        driver_face_recognition_btn = root.findViewById(R.id.driver_face_recognition_btn);
        speech_recognition_btn = root.findViewById(R.id.speech_recognition_btn);
        cards_btn = root.findViewById(R.id.cards_btn);
        anxiety_btn = root.findViewById(R.id.anxiety_btn);
        test_btn = root.findViewById(R.id.test_btn);
        cogni_btn = root.findViewById(R.id.cogni_btn);
        mAuth = FirebaseAuth.getInstance();
        face_recognition_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        speech_recognition_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), speech_emotion_recognizer.class);
            startActivity(intent);
        });
        cards_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CardsActivity.class);
            startActivity(intent);
        });
        anxiety_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnxietiesActivity.class);
            startActivity(intent);
        });
        test_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TestActivity.class);
            startActivity(intent);
        });
        cogni_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CogniTestActivity.class);
            startActivity(intent);
        });
//        driver_face_recognition_btn.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), DriverFaceRecognitionActivity.class);
//            startActivity(intent);
//        });
        driver_face_recognition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Введите фразу");
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.enter_tts, null);
                builder.setView(dialogView);
                EditText phraseEditText = dialogView.findViewById(R.id.phraseEditText);
                builder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phrase = phraseEditText.getText().toString();
                        if(TextUtils.isEmpty(phrase)){
                            Intent intent = new Intent(getActivity(), DriverFaceRecognitionActivity.class);
                            startActivity(intent);
                        }
                        else {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String uid = mAuth.getCurrentUser().getUid();
                                String path = "users/" + uid + "/phrase_when_driving/";
                                DatabaseReference userEmotionsRef = database.getReference(path);
                                userEmotionsRef.setValue(phrase)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(getActivity(), DriverFaceRecognitionActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Ошибка при сохранении данных в базе данных Firebase.", e);
                                            }
                                        });
                            }
                            else{
                                Intent intent = new Intent(getActivity(), DriverFaceRecognitionActivity.class);
                                intent.putExtra("phrase", phrase);
                                startActivity(intent);
                            }
                        }
                    }
                });
                builder.setNeutralButton("Сбросить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String uid = mAuth.getCurrentUser().getUid();
                            String path = "users/" + uid + "/phrase_when_driving/";
                            DatabaseReference userEmotionsRef = database.getReference(path);
                            userEmotionsRef.removeValue();
                        }
                    }
                });
                builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}