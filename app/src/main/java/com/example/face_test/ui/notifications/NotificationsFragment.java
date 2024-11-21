package com.example.face_test.ui.notifications;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.face_test.ChatsActivity;
import com.example.face_test.DriverFaceRecognitionActivity;
import com.example.face_test.MainActivity2;
import com.example.face_test.RegistrationActivity;
import com.example.face_test.R;
import com.example.face_test.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NotificationsFragment extends Fragment {

private FragmentNotificationsBinding binding;
    private FirebaseAuth mAuth;
    private String doctorId;
    private View root;
    private String[] emotionLabels = {"Удивление", "Страх", "Злость", "Спокойствие", "Грусть", "Неприязнь", "Счастье"};
    private String[] emotionTest = {"Принятие", "Бодрость", "Тонус", "Открытость", "Покой", "Стойкость", "Довольство", "Самооценка"};
    private String[] cogniTest = {"Настройка", "Мысли", "Упорство", "Этика", "Катастрофа", "Бессилие", "Экстремизм", "Драма", "Гипернорма"};
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        root = binding.getRoot();
        if (currentUser != null) {
            EditText editTextEmail = root.findViewById(R.id.editTextEmail);
            TextView textAuth = root.findViewById(R.id.textAuth);
            TextView reset_password_text_view = root.findViewById(R.id.reset_password_text_view);
            EditText editTextPassword = root.findViewById(R.id.editTextPassword);
            Button buttonContinue = root.findViewById(R.id.buttonContinue);
            Button buttonRegistration = root.findViewById(R.id.buttonRegistration);
            Button button_exit = root.findViewById(R.id.button_exit);
            Button button_share = root.findViewById(R.id.button_share);
            Button patientsButton = root.findViewById(R.id.patientsButton);
            Button adminButton = root.findViewById(R.id.adminButton);
            Button chatButton = root.findViewById(R.id.chatButton);
            TextView faceTextView, emotionTextView, cogniTextView;
            TableLayout emotionTableLayout, emotionTestTableLayout, cogniTestTableLayout;
            faceTextView = root.findViewById(R.id.faceTextView);
            emotionTableLayout = root.findViewById(R.id.emotionTableLayout);
            emotionTextView = root.findViewById(R.id.emotionTextView);
            emotionTestTableLayout = root.findViewById(R.id.emotionTestTableLayout);
            cogniTextView = root.findViewById(R.id.cogniTextView);
            cogniTestTableLayout = root.findViewById(R.id.cogniTestTableLayout);
            String userId = mAuth.getCurrentUser().getUid();
            editTextEmail.setVisibility(View.GONE);
            editTextPassword.setVisibility(View.GONE);
            reset_password_text_view.setVisibility(View.GONE);
            buttonContinue.setVisibility(View.GONE);
            buttonRegistration.setVisibility(View.GONE);
            textAuth.setVisibility(View.GONE);
            DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");
            userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.getValue(String.class);
                        Log.d("UserRole", "Роль пользователя: " + userRole);
                        DatabaseReference userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(mAuth.getCurrentUser().getUid()).child("faceRecognition");
                        userEmotionsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                TableLayout emotionTableLayout = root.findViewById(R.id.emotionTableLayout);
                                emotionTableLayout.removeAllViews();
                                if(getContext()!=null) {
                                    TableRow row1 = new TableRow(getContext());
                                    TextView textView1 = new TextView(getContext());
                                    textView1.setText("Время");
                                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                    textView1.setGravity(Gravity.CENTER);
                                    row1.addView(textView1);
                                    for(int i=0;i<emotionLabels.length;i++){
                                        TextView emotionTextView1 = new TextView(getContext());
                                        emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                        emotionTextView1.setGravity(Gravity.CENTER);
                                        emotionTextView1.setText(emotionLabels[i]);
                                        row1.addView(emotionTextView1);
                                    }
                                    emotionTableLayout.addView(row1);
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String time = snapshot.getKey();
                                        long timestamp = Long.parseLong(time);
                                        Date date = new Date(timestamp);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                        String formattedTime = sdf.format(date);
                                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                        };
                                        List<String> emotionsList = snapshot.getValue(t);
                                        TableRow row = new TableRow(getContext());
                                        TextView timeTextView = new TextView(getContext());
                                        timeTextView.setText(formattedTime);
                                        timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                        timeTextView.setGravity(Gravity.CENTER);
                                        row.addView(timeTextView);
                                        for (String emotion : emotionsList) {
                                            TextView emotionTextView = new TextView(getContext());
                                            emotionTextView.setText(emotion);
                                            emotionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                            emotionTextView.setGravity(Gravity.CENTER);
                                            row.addView(emotionTextView);
                                        }
                                        emotionTableLayout.addView(row);
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                            }
                        });
                        userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(mAuth.getCurrentUser().getUid()).child("emotionTest");
                        userEmotionsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                TableLayout emotionTestTableLayout = root.findViewById(R.id.emotionTestTableLayout);
                                emotionTestTableLayout.removeAllViews();
                                if(getContext()!=null) {
                                        TableRow row1 = new TableRow(getContext());
                                        TextView textView1 = new TextView(getContext());
                                        textView1.setText("Время");
                                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                        textView1.setGravity(Gravity.CENTER);
                                        row1.addView(textView1);
                                        for (int i = 0; i < emotionTest.length; i++) {
                                            TextView emotionTextView1 = new TextView(getContext());
                                            emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                            emotionTextView1.setGravity(Gravity.CENTER);
                                            emotionTextView1.setText(emotionTest[i]);
                                            row1.addView(emotionTextView1);
                                        }
                                        emotionTestTableLayout.addView(row1);
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String time = snapshot.getKey();
                                            long timestamp = Long.parseLong(time);
                                            Date date = new Date(timestamp);
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                            String formattedTime = sdf.format(date);
                                            List<Integer> tScoresList = new ArrayList<>();
                                            TableRow row = new TableRow(getContext());
                                            TextView timeTextView = new TextView(getContext());
                                            timeTextView.setText(formattedTime);
                                            timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                            timeTextView.setGravity(Gravity.CENTER);
                                            row.addView(timeTextView);
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                if (childSnapshot.getKey().equals("tScores")) {
                                                    GenericTypeIndicator<List<Integer>> tScoresType = new GenericTypeIndicator<List<Integer>>() {
                                                    };
                                                    tScoresList = childSnapshot.getValue(tScoresType);
                                                    for (Integer tScore : tScoresList) {
                                                        TextView tScoresTextView = new TextView(getContext());
                                                        tScoresTextView.setText(String.valueOf(tScore));
                                                        tScoresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                        tScoresTextView.setGravity(Gravity.CENTER);
                                                        row.addView(tScoresTextView);
                                                    }
                                                }
                                            }
                                            emotionTestTableLayout.addView(row);
                                        }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                            }
                        });
                        userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(mAuth.getCurrentUser().getUid()).child("cogniTest");
                        userEmotionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                TableLayout cogniTestTableLayout = root.findViewById(R.id.cogniTestTableLayout);
                                cogniTestTableLayout.removeAllViews();
                                if (getContext() != null) {
                                    TableRow row1 = new TableRow(getContext());
                                    TextView textView1 = new TextView(getContext());
                                    textView1.setText("Время");
                                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                    textView1.setGravity(Gravity.CENTER);
                                    row1.addView(textView1);
                                    for (int i = 0; i < cogniTest.length; i++) {
                                        TextView emotionTextView1 = new TextView(getContext());
                                        emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                        emotionTextView1.setGravity(Gravity.CENTER);
                                        emotionTextView1.setText(cogniTest[i]);
                                        row1.addView(emotionTextView1);
                                    }
                                    cogniTestTableLayout.addView(row1);
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String time = snapshot.getKey();
                                        long timestamp = Long.parseLong(time);
                                        Date date = new Date(timestamp);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                        String formattedTime = sdf.format(date);
                                        List<Float> rawScoresList = snapshot.child("scores").getValue(new GenericTypeIndicator<List<Float>>() {
                                        });
                                        TableRow row = new TableRow(getContext());
                                        TextView timeTextView = new TextView(getContext());
                                        timeTextView.setText(formattedTime);
                                        row.addView(timeTextView);
                                        for (Float score : rawScoresList) {
                                            TextView scoresTextView = new TextView(getContext());
                                            scoresTextView.setText(String.valueOf(score));
                                            timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                            timeTextView.setGravity(Gravity.CENTER);
                                            scoresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                            scoresTextView.setGravity(Gravity.CENTER);
                                            row.addView(scoresTextView);
                                        }
                                        cogniTestTableLayout.addView(row);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Ошибка при чтении данных из Firebase.", databaseError.toException());
                            }
                        });
                        if (Objects.equals(userRole, "patient")) {
                            button_exit.setVisibility(View.VISIBLE);
                            button_share.setVisibility(View.VISIBLE);
                            faceTextView.setVisibility(View.VISIBLE);
                            emotionTableLayout.setVisibility(View.VISIBLE);
                            emotionTextView.setVisibility(View.VISIBLE);
                            emotionTestTableLayout.setVisibility(View.VISIBLE);
                            cogniTextView.setVisibility(View.VISIBLE);
                            cogniTestTableLayout.setVisibility(View.VISIBLE);
                            patientsButton.setVisibility(View.GONE);
                            adminButton.setVisibility(View.GONE);
                            chatButton.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.addRule(RelativeLayout.BELOW, R.id.linear_layout5);
                            faceTextView.setLayoutParams(params);
                        } else if (Objects.equals(userRole, "doctor")) {
                            button_exit.setVisibility(View.VISIBLE);
                            editTextEmail.setVisibility(View.GONE);
                            editTextPassword.setVisibility(View.GONE);
                            reset_password_text_view.setVisibility(View.GONE);
                            buttonContinue.setVisibility(View.GONE);
                            buttonRegistration.setVisibility(View.GONE);
                            textAuth.setVisibility(View.GONE);
                            patientsButton.setVisibility(View.VISIBLE);
                            adminButton.setVisibility(View.GONE);
                            faceTextView.setVisibility(View.VISIBLE);
                            emotionTableLayout.setVisibility(View.VISIBLE);
                            emotionTextView.setVisibility(View.VISIBLE);
                            emotionTestTableLayout.setVisibility(View.VISIBLE);
                            cogniTextView.setVisibility(View.VISIBLE);
                            cogniTestTableLayout.setVisibility(View.VISIBLE);
                            chatButton.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.addRule(RelativeLayout.BELOW, R.id.linear_layout5);
                            faceTextView.setLayoutParams(params);
                        } else if (Objects.equals(userRole, "admin")){
                            button_exit.setVisibility(View.VISIBLE);
                            editTextEmail.setVisibility(View.GONE);
                            editTextPassword.setVisibility(View.GONE);
                            reset_password_text_view.setVisibility(View.GONE);
                            buttonContinue.setVisibility(View.GONE);
                            buttonRegistration.setVisibility(View.GONE);
                            textAuth.setVisibility(View.GONE);
                            patientsButton.setVisibility(View.GONE);
                            adminButton.setVisibility(View.VISIBLE);
                            button_share.setVisibility(View.VISIBLE);
                            faceTextView.setVisibility(View.VISIBLE);
                            emotionTableLayout.setVisibility(View.VISIBLE);
                            emotionTextView.setVisibility(View.VISIBLE);
                            emotionTestTableLayout.setVisibility(View.VISIBLE);
                            cogniTextView.setVisibility(View.VISIBLE);
                            cogniTestTableLayout.setVisibility(View.VISIBLE);
                            chatButton.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.addRule(RelativeLayout.BELOW, R.id.adminButton);
                            faceTextView.setLayoutParams(params);
                        }
                    } else {
                        Log.d("UserRole", "Роль пользователя не найдена");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Не удалось получить данные", error.toException());
                }
            });
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
    binding = FragmentNotificationsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        TextView textAuth = root.findViewById(R.id.textAuth);
        TextView reset_password_text_view = root.findViewById(R.id.reset_password_text_view);
        reset_password_text_view.setPaintFlags(reset_password_text_view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        EditText editTextEmail = root.findViewById(R.id.editTextEmail);
        EditText editTextPassword = root.findViewById(R.id.editTextPassword);
        Button buttonContinue = root.findViewById(R.id.buttonContinue);
        Button buttonRegistration = root.findViewById(R.id.buttonRegistration);
        Button chatButton = root.findViewById(R.id.chatButton);
        Button button_exit = root.findViewById(R.id.button_exit);
        Button button_share = root.findViewById(R.id.button_share);
        Button patientsButton = root.findViewById(R.id.patientsButton);
        Button adminButton = root.findViewById(R.id.adminButton);
        patientsButton.setVisibility(View.GONE);
        adminButton.setVisibility(View.GONE);
        chatButton.setVisibility(View.GONE);
        button_exit.setVisibility(View.GONE);
        button_share.setVisibility(View.INVISIBLE);
        TextView faceTextView, emotionTextView, cogniTextView;
        TableLayout emotionTableLayout, emotionTestTableLayout, cogniTestTableLayout;
        faceTextView = root.findViewById(R.id.faceTextView);
        faceTextView.setVisibility(View.GONE);
        emotionTableLayout = root.findViewById(R.id.emotionTableLayout);
        emotionTableLayout.setVisibility(View.GONE);
        emotionTextView = root.findViewById(R.id.emotionTextView);
        emotionTextView.setVisibility(View.GONE);
        emotionTestTableLayout = root.findViewById(R.id.emotionTestTableLayout);
        emotionTestTableLayout.setVisibility(View.GONE);
        cogniTextView = root.findViewById(R.id.cogniTextView);
        cogniTextView.setVisibility(View.GONE);
        cogniTestTableLayout = root.findViewById(R.id.cogniTestTableLayout);
        cogniTestTableLayout.setVisibility(View.GONE);
        reset_password_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Сброс пароля");
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.reset_password_layout, null);
                builder.setView(dialogView);
                EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
                builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = emailEditText.getText().toString();
                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(getContext(),"Введите почту", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),"Инструкции по сбросу пароля\nотправлены на вашу почту.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
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
        buttonContinue.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            Activity activity = getActivity();
            if (activity != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(getContext(),"Введите почту", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(getContext(),"Введите пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Успешная авторизация!",
                                         Toast.LENGTH_SHORT).show();
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");
                                userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String userRole = dataSnapshot.getValue(String.class);
                                            Log.d("UserRole", "Роль пользователя: " + userRole);
                                            DatabaseReference userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(mAuth.getCurrentUser().getUid()).child("faceRecognition");
                                            userEmotionsRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    TableLayout emotionTableLayout = root.findViewById(R.id.emotionTableLayout);
                                                    emotionTableLayout.removeAllViews();
                                                    if(getContext()!=null) {
                                                        TableRow row1 = new TableRow(getContext());
                                                        TextView textView1 = new TextView(getContext());
                                                        textView1.setText("Время");
                                                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                        textView1.setGravity(Gravity.CENTER);
                                                        row1.addView(textView1);
                                                        for(int i=0;i<emotionLabels.length;i++){
                                                            TextView emotionTextView1 = new TextView(getContext());
                                                            emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            emotionTextView1.setGravity(Gravity.CENTER);
                                                            emotionTextView1.setText(emotionLabels[i]);
                                                            row1.addView(emotionTextView1);
                                                        }
                                                        emotionTableLayout.addView(row1);
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String time = snapshot.getKey();
                                                            long timestamp = Long.parseLong(time);
                                                            Date date = new Date(timestamp);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                                            String formattedTime = sdf.format(date);
                                                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                                            };
                                                            List<String> emotionsList = snapshot.getValue(t);
                                                            TableRow row = new TableRow(getContext());
                                                            TextView timeTextView = new TextView(getContext());
                                                            timeTextView.setText(formattedTime);
                                                            timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            timeTextView.setGravity(Gravity.CENTER);
                                                            row.addView(timeTextView);
                                                            for (String emotion : emotionsList) {
                                                                TextView emotionTextView = new TextView(getContext());
                                                                emotionTextView.setText(emotion);
                                                                emotionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                emotionTextView.setGravity(Gravity.CENTER);
                                                                row.addView(emotionTextView);
                                                            }
                                                            emotionTableLayout.addView(row);
                                                        }
                                                    }
                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                                                }
                                            });
                                            userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(mAuth.getCurrentUser().getUid()).child("emotionTest");
                                            userEmotionsRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    TableLayout emotionTestTableLayout = root.findViewById(R.id.emotionTestTableLayout);
                                                    emotionTestTableLayout.removeAllViews();
                                                    if (getContext() != null) {
                                                        TableRow row1 = new TableRow(getContext());
                                                        TextView textView1 = new TextView(getContext());
                                                        textView1.setText("Время");
                                                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                        textView1.setGravity(Gravity.CENTER);
                                                        row1.addView(textView1);
                                                        for (int i = 0; i < emotionTest.length; i++) {
                                                            TextView emotionTextView1 = new TextView(getContext());
                                                            emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            emotionTextView1.setGravity(Gravity.CENTER);
                                                            emotionTextView1.setText(emotionTest[i]);
                                                            row1.addView(emotionTextView1);
                                                        }
                                                        emotionTestTableLayout.addView(row1);
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String time = snapshot.getKey();
                                                            long timestamp = Long.parseLong(time);
                                                            Date date = new Date(timestamp);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                                            String formattedTime = sdf.format(date);
                                                            List<Integer> tScoresList = new ArrayList<>();
                                                            TableRow row = new TableRow(getContext());
                                                            TextView timeTextView = new TextView(getContext());
                                                            timeTextView.setText(formattedTime);
                                                            timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            timeTextView.setGravity(Gravity.CENTER);
                                                            row.addView(timeTextView);
                                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                if (childSnapshot.getKey().equals("tScores")) {
                                                                    GenericTypeIndicator<List<Integer>> tScoresType = new GenericTypeIndicator<List<Integer>>() {
                                                                    };
                                                                    tScoresList = childSnapshot.getValue(tScoresType);
                                                                    for (Integer tScore : tScoresList) {
                                                                        TextView tScoresTextView = new TextView(getContext());
                                                                        tScoresTextView.setText(String.valueOf(tScore));
                                                                        tScoresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                        tScoresTextView.setGravity(Gravity.CENTER);
                                                                        row.addView(tScoresTextView);
                                                                    }
                                                                }
                                                            }
                                                            emotionTestTableLayout.addView(row);
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                                                }
                                            });
                                            userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(mAuth.getCurrentUser().getUid()).child("cogniTest");
                                            userEmotionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    TableLayout cogniTestTableLayout = root.findViewById(R.id.cogniTestTableLayout);
                                                    cogniTestTableLayout.removeAllViews();
                                                    if (getContext() != null) {
                                                        TableRow row1 = new TableRow(getContext());
                                                        TextView textView1 = new TextView(getContext());
                                                        textView1.setText("Время");
                                                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                        textView1.setGravity(Gravity.CENTER);
                                                        row1.addView(textView1);
                                                        for (int i = 0; i < cogniTest.length; i++) {
                                                            TextView emotionTextView1 = new TextView(getContext());
                                                            emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            emotionTextView1.setGravity(Gravity.CENTER);
                                                            emotionTextView1.setText(cogniTest[i]);
                                                            row1.addView(emotionTextView1);
                                                        }
                                                        cogniTestTableLayout.addView(row1);
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String time = snapshot.getKey();
                                                            long timestamp = Long.parseLong(time);
                                                            Date date = new Date(timestamp);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                                            String formattedTime = sdf.format(date);
                                                            List<Float> rawScoresList = snapshot.child("scores").getValue(new GenericTypeIndicator<List<Float>>() {
                                                            });
                                                            TableRow row = new TableRow(getContext());
                                                            TextView timeTextView = new TextView(getContext());
                                                            timeTextView.setText(formattedTime);
                                                            row.addView(timeTextView);
                                                            for (Float score : rawScoresList) {
                                                                TextView scoresTextView = new TextView(getContext());
                                                                scoresTextView.setText(String.valueOf(score));
                                                                timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                timeTextView.setGravity(Gravity.CENTER);
                                                                scoresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                scoresTextView.setGravity(Gravity.CENTER);
                                                                row.addView(scoresTextView);
                                                            }
                                                            cogniTestTableLayout.addView(row);
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Log.e("Firebase", "Ошибка при чтении данных из Firebase.", databaseError.toException());
                                                }
                                            });
                                            if (Objects.equals(userRole, "patient")) {
                                                button_exit.setVisibility(View.VISIBLE);
                                                button_share.setVisibility(View.VISIBLE);
                                                faceTextView.setVisibility(View.VISIBLE);
                                                emotionTableLayout.setVisibility(View.VISIBLE);
                                                emotionTextView.setVisibility(View.VISIBLE);
                                                emotionTestTableLayout.setVisibility(View.VISIBLE);
                                                cogniTextView.setVisibility(View.VISIBLE);
                                                cogniTestTableLayout.setVisibility(View.VISIBLE);
                                                editTextEmail.setVisibility(View.GONE);
                                                reset_password_text_view.setVisibility(View.GONE);
                                                editTextPassword.setVisibility(View.GONE);
                                                buttonContinue.setVisibility(View.GONE);
                                                buttonRegistration.setVisibility(View.GONE);
                                                textAuth.setVisibility(View.GONE);
                                                patientsButton.setVisibility(View.GONE);
                                                adminButton.setVisibility(View.GONE);
                                                chatButton.setVisibility(View.VISIBLE);
                                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT
                                                );
                                                params.addRule(RelativeLayout.BELOW, R.id.linear_layout5);
                                                faceTextView.setLayoutParams(params);
                                            } else if (Objects.equals(userRole, "doctor")) {
                                                button_exit.setVisibility(View.VISIBLE);
                                                editTextEmail.setVisibility(View.GONE);
                                                reset_password_text_view.setVisibility(View.GONE);
                                                editTextPassword.setVisibility(View.GONE);
                                                buttonContinue.setVisibility(View.GONE);
                                                buttonRegistration.setVisibility(View.GONE);
                                                textAuth.setVisibility(View.GONE);
                                                patientsButton.setVisibility(View.VISIBLE);
                                                adminButton.setVisibility(View.GONE);
                                                faceTextView.setVisibility(View.VISIBLE);
                                                emotionTableLayout.setVisibility(View.VISIBLE);
                                                emotionTextView.setVisibility(View.VISIBLE);
                                                emotionTestTableLayout.setVisibility(View.VISIBLE);
                                                cogniTextView.setVisibility(View.VISIBLE);
                                                cogniTestTableLayout.setVisibility(View.VISIBLE);
                                                chatButton.setVisibility(View.VISIBLE);
                                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT
                                                );
                                                params.addRule(RelativeLayout.BELOW, R.id.linear_layout5);
                                                faceTextView.setLayoutParams(params);
                                            } else if (Objects.equals(userRole, "admin")){
                                                button_exit.setVisibility(View.VISIBLE);
                                                button_share.setVisibility(View.VISIBLE);
                                                editTextEmail.setVisibility(View.GONE);
                                                reset_password_text_view.setVisibility(View.GONE);
                                                editTextPassword.setVisibility(View.GONE);
                                                buttonContinue.setVisibility(View.GONE);
                                                buttonRegistration.setVisibility(View.GONE);
                                                textAuth.setVisibility(View.GONE);
                                                patientsButton.setVisibility(View.GONE);
                                                adminButton.setVisibility(View.VISIBLE);
                                                faceTextView.setVisibility(View.VISIBLE);
                                                emotionTableLayout.setVisibility(View.VISIBLE);
                                                emotionTextView.setVisibility(View.VISIBLE);
                                                emotionTestTableLayout.setVisibility(View.VISIBLE);
                                                cogniTextView.setVisibility(View.VISIBLE);
                                                cogniTestTableLayout.setVisibility(View.VISIBLE);
                                                chatButton.setVisibility(View.VISIBLE);
                                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT
                                                );
                                                params.addRule(RelativeLayout.BELOW, R.id.adminButton);
                                                faceTextView.setLayoutParams(params);
                                            }
                                        } else {
                                            Log.d("UserRole", "Роль пользователя не найдена");
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Firebase", "Не удалось получить данные", error.toException());
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Неверная почта или пароль!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });
        buttonRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(root.getContext(), RegistrationActivity.class);
            startActivity(intent);
        });
        chatButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(root.getContext(), ChatsActivity.class);
                startActivity(intent);
            }
        });
        button_exit.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            faceTextView.setVisibility(View.GONE);
            emotionTableLayout.setVisibility(View.GONE);
            emotionTextView.setVisibility(View.GONE);
            emotionTestTableLayout.setVisibility(View.GONE);
            cogniTextView.setVisibility(View.GONE);
            cogniTestTableLayout.setVisibility(View.GONE);
            button_exit.setVisibility(View.GONE);
            button_share.setVisibility(View.INVISIBLE);
            editTextEmail.setVisibility(View.VISIBLE);
            reset_password_text_view.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);
            buttonContinue.setVisibility(View.VISIBLE);
            buttonRegistration.setVisibility(View.VISIBLE);
            textAuth.setVisibility(View.VISIBLE);
            patientsButton.setVisibility(View.GONE);
            adminButton.setVisibility(View.GONE);
            chatButton.setVisibility(View.GONE);
        });
        button_share.setOnClickListener(v -> {
            openDoctorSelectionDialog();
        });
        patientsButton.setOnClickListener(v -> {
            openPatientSelectionDialog();
        });
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Введите данные");
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.admin_dialog_layout, null);
                builder.setView(dialogView);
                EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
                EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
                EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
                EditText surnameEditText = dialogView.findViewById(R.id.surnameEditText);
                builder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        String name = nameEditText.getText().toString();
                        String surname = surnameEditText.getText().toString();
                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(root.getContext(),"Введите почту!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(password)){
                            Toast.makeText(root.getContext(),"Введите пароль!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(name)){
                            Toast.makeText(root.getContext(),"Введите имя!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(surname)){
                            Toast.makeText(root.getContext(),"Введите фамилию!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                String userId = user.getUid();
                                                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role").setValue("doctor");
                                                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("name_surname").setValue(name + " " + surname);
                                                FirebaseDatabase.getInstance().getReference().child("doctors").child(userId).child("name_surname").setValue(name + " " + surname);
                                                Toast.makeText(getContext(), "Пользователь успешно создан", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e("Firebase", "Ошибка: пользователь не найден");
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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

    private void openDoctorSelectionDialog() {
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference().child("doctors");
        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> doctorNames = new ArrayList<>();
                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    String name_surname = doctorSnapshot.child("name_surname").getValue(String.class);
                    doctorNames.add(name_surname);
                }
                String[] doctorsArray = doctorNames.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Выберите врача");
                builder.setItems(doctorsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedDoctor = doctorsArray[which];
                        shareDataWithDoctor(selectedDoctor);
                    }
                });
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка при получении списка врачей: " + databaseError.getMessage());
            }
        });
    }



    private void openPatientSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите пациента");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference sharedPatientsRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("sharedPatients");
        sharedPatientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> patientUids = new ArrayList<>();
                    for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                        String patientUid = patientSnapshot.getKey();
                        patientUids.add(patientUid);
                    }
                    List<String> patientNames = new ArrayList<>();
                    for (String patientUid : patientUids) {
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(patientUid);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String name = dataSnapshot.child("name_surname").getValue(String.class);
                                    String fullName = name;
                                    patientNames.add(fullName);
                                    if (patientNames.size() == patientUids.size()) {
                                        String[] patientNamesArray = patientNames.toArray(new String[0]);
                                        builder.setItems(patientNamesArray, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String selectedPatientUid = patientUids.get(which);
                                                TextView faceTextView, emotionTextView, cogniTextView;
                                                TableLayout emotionTableLayout, emotionTestTableLayout, cogniTestTableLayout;
                                                faceTextView = root.findViewById(R.id.faceTextView);
                                                faceTextView.setVisibility(View.GONE);
                                                emotionTableLayout = root.findViewById(R.id.emotionTableLayout);
                                                emotionTableLayout.setVisibility(View.GONE);
                                                emotionTextView = root.findViewById(R.id.emotionTextView);
                                                emotionTextView.setVisibility(View.GONE);
                                                emotionTestTableLayout = root.findViewById(R.id.emotionTestTableLayout);
                                                emotionTestTableLayout.setVisibility(View.GONE);
                                                cogniTextView = root.findViewById(R.id.cogniTextView);
                                                cogniTextView.setVisibility(View.GONE);
                                                cogniTestTableLayout = root.findViewById(R.id.cogniTestTableLayout);
                                                cogniTestTableLayout.setVisibility(View.GONE);
                                                faceTextView.setVisibility(View.VISIBLE);
                                                emotionTableLayout.setVisibility(View.VISIBLE);
                                                emotionTextView.setVisibility(View.VISIBLE);
                                                emotionTestTableLayout.setVisibility(View.VISIBLE);
                                                cogniTextView.setVisibility(View.VISIBLE);
                                                cogniTestTableLayout.setVisibility(View.VISIBLE);
                                                DatabaseReference userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(selectedPatientUid).child("faceRecognition");
                                                userEmotionsRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        TableLayout emotionTableLayout = root.findViewById(R.id.emotionTableLayout);
                                                        emotionTableLayout.removeAllViews();
                                                        if(getContext()!=null) {
                                                            TableRow row1 = new TableRow(getContext());
                                                            TextView textView1 = new TextView(getContext());
                                                            textView1.setText("Время");
                                                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            textView1.setGravity(Gravity.CENTER);
                                                            row1.addView(textView1);
                                                            for(int i=0;i<emotionLabels.length;i++){
                                                                TextView emotionTextView1 = new TextView(getContext());
                                                                emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                emotionTextView1.setGravity(Gravity.CENTER);
                                                                emotionTextView1.setText(emotionLabels[i]);
                                                                row1.addView(emotionTextView1);
                                                            }
                                                            emotionTableLayout.addView(row1);
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                String time = snapshot.getKey();
                                                                long timestamp = Long.parseLong(time);
                                                                Date date = new Date(timestamp);
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                                                String formattedTime = sdf.format(date);
                                                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                                                };
                                                                List<String> emotionsList = snapshot.getValue(t);
                                                                TableRow row = new TableRow(getContext());
                                                                TextView timeTextView = new TextView(getContext());
                                                                timeTextView.setText(formattedTime);
                                                                timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                timeTextView.setGravity(Gravity.CENTER);
                                                                row.addView(timeTextView);
                                                                for (String emotion : emotionsList) {
                                                                    TextView emotionTextView = new TextView(getContext());
                                                                    emotionTextView.setText(emotion);
                                                                    emotionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                    emotionTextView.setGravity(Gravity.CENTER);
                                                                    row.addView(emotionTextView);
                                                                }
                                                                emotionTableLayout.addView(row);
                                                            }
                                                        }
                                                    }


                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                                                    }
                                                });
                                                userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(selectedPatientUid).child("emotionTest");
                                                userEmotionsRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        TableLayout emotionTestTableLayout = root.findViewById(R.id.emotionTestTableLayout);
                                                        emotionTestTableLayout.removeAllViews();
                                                        if(getContext()!=null) {
                                                            TableRow row1 = new TableRow(getContext());
                                                            TextView textView1 = new TextView(getContext());
                                                            textView1.setText("Время");
                                                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            textView1.setGravity(Gravity.CENTER);
                                                            row1.addView(textView1);
                                                            for (int i = 0; i < emotionTest.length; i++) {
                                                                TextView emotionTextView1 = new TextView(getContext());
                                                                emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                emotionTextView1.setGravity(Gravity.CENTER);
                                                                emotionTextView1.setText(emotionTest[i]);
                                                                row1.addView(emotionTextView1);
                                                            }
                                                            emotionTestTableLayout.addView(row1);
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                String time = snapshot.getKey();
                                                                long timestamp = Long.parseLong(time);
                                                                Date date = new Date(timestamp);
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                                                String formattedTime = sdf.format(date);
                                                                List<Integer> tScoresList = new ArrayList<>();
                                                                TableRow row = new TableRow(getContext());
                                                                TextView timeTextView = new TextView(getContext());
                                                                timeTextView.setText(formattedTime);
                                                                timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                timeTextView.setGravity(Gravity.CENTER);
                                                                row.addView(timeTextView);
                                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                    if (childSnapshot.getKey().equals("tScores")) {
                                                                        GenericTypeIndicator<List<Integer>> tScoresType = new GenericTypeIndicator<List<Integer>>() {
                                                                        };
                                                                        tScoresList = childSnapshot.getValue(tScoresType);
                                                                        for (Integer tScore : tScoresList) {
                                                                            TextView tScoresTextView = new TextView(getContext());
                                                                            tScoresTextView.setText(String.valueOf(tScore));
                                                                            tScoresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                            tScoresTextView.setGravity(Gravity.CENTER);
                                                                            row.addView(tScoresTextView);
                                                                        }
                                                                    }
                                                                }
                                                                emotionTestTableLayout.addView(row);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                                                    }
                                                });
                                                userEmotionsRef = FirebaseDatabase.getInstance().getReference("users_emotions").child(selectedPatientUid).child("cogniTest");
                                                userEmotionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        TableLayout cogniTestTableLayout = root.findViewById(R.id.cogniTestTableLayout);
                                                        cogniTestTableLayout.removeAllViews();
                                                        if (getContext() != null) {
                                                            TableRow row1 = new TableRow(getContext());
                                                            TextView textView1 = new TextView(getContext());
                                                            textView1.setText("Время");
                                                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                            textView1.setGravity(Gravity.CENTER);
                                                            row1.addView(textView1);
                                                            for (int i = 0; i < cogniTest.length; i++) {
                                                                TextView emotionTextView1 = new TextView(getContext());
                                                                emotionTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                emotionTextView1.setGravity(Gravity.CENTER);
                                                                emotionTextView1.setText(cogniTest[i]);
                                                                row1.addView(emotionTextView1);
                                                            }
                                                            cogniTestTableLayout.addView(row1);
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                String time = snapshot.getKey();
                                                                long timestamp = Long.parseLong(time);
                                                                Date date = new Date(timestamp);
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
                                                                String formattedTime = sdf.format(date);
                                                                List<Float> rawScoresList = snapshot.child("scores").getValue(new GenericTypeIndicator<List<Float>>() {
                                                                });
                                                                TableRow row = new TableRow(getContext());
                                                                TextView timeTextView = new TextView(getContext());
                                                                timeTextView.setText(formattedTime);
                                                                row.addView(timeTextView);
                                                                for (Float score : rawScoresList) {
                                                                    TextView scoresTextView = new TextView(getContext());
                                                                    scoresTextView.setText(String.valueOf(score));
                                                                    timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                    timeTextView.setGravity(Gravity.CENTER);
                                                                    scoresTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                                                                    scoresTextView.setGravity(Gravity.CENTER);
                                                                    row.addView(scoresTextView);
                                                                }
                                                                cogniTestTableLayout.addView(row);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.e("Firebase", "Ошибка при чтении данных из Firebase.", databaseError.toException());
                                                    }
                                                });
                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Ошибка при получении данных пользователя: " + databaseError.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("Firebase", "Список пациентов пуст");
                    Toast.makeText(root.getContext(), "Список пациентов пуст",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка при получении списка пациентов: " + databaseError.getMessage());
            }
        });
    }

    private void shareDataWithDoctor(String doctorName_Surname) {
        Query query = FirebaseDatabase.getInstance().getReference().child("doctors").orderByChild("name_surname").equalTo(doctorName_Surname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot doctorSnapshot = dataSnapshot.getChildren().iterator().next();
                    doctorId = doctorSnapshot.getKey();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        FirebaseDatabase.getInstance().getReference()
                                .child("users").child(doctorId)
                                .child("sharedPatients").child(userId).setValue(true);
                        Toast.makeText(getContext(), "Данные успешно переданы врачу", Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "Данные успешно переданы врачу");
                    } else {
                        Log.e("Firebase", "Пользователь не аутентифицирован");
                    }
                } else {
                    Log.e("Firebase", "Врач с именем " + doctorName_Surname + " не найден");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка при выполнении запроса к базе данных", databaseError.toException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}