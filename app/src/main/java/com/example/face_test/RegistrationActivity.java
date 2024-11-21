package com.example.face_test;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private String userRole="patient";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextSecondName = findViewById(R.id.editTextSecondName);
        Button buttonContinue = findViewById(R.id.buttonContinue);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button button_close = findViewById(R.id.button_close);
        buttonContinue.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String name = editTextName.getText().toString();
            String second_name = editTextSecondName.getText().toString();
            if(TextUtils.isEmpty(email)){
                Toast.makeText(RegistrationActivity.this,"Введите почту!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(RegistrationActivity.this,"Введите пароль!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(name)){
                Toast.makeText(RegistrationActivity.this,"Введите имя!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(second_name)){
                Toast.makeText(RegistrationActivity.this,"Введите фамилию!", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();
                                FirebaseDatabase.getInstance().getReference().child("users").
                                        child(userId).child("role").setValue(userRole);
                                FirebaseDatabase.getInstance().getReference().child("users").
                                        child(userId).child("name_surname").setValue(name+" "+second_name);
                                Toast.makeText(RegistrationActivity.this, "Успешная регистрация!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                                intent.putExtra("selectedTab", R.id.navigation_notifications);
                                startActivity(intent);
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Неверная почта или пароль!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_notifications);
            startActivity(intent);
        });
        button_close.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                userRole = "patient";
                break;
            case 1:
                userRole = "doctor";
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}