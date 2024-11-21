package com.example.face_test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.face_test.ui.dashboard.DashboardFragment;

public class AnxietiesActivity extends AppCompatActivity {
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxieties);
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_dashboard);
            startActivity(intent);
        });
        Button anxiety_is_btn = findViewById(R.id.anxiety_is_btn);
        anxiety_is_btn.setOnClickListener(v -> {
            showDialogAnxiety_is();
        });
        Button stages_of_anxiety_development_btn = findViewById(R.id.stages_of_anxiety_development_btn);
        stages_of_anxiety_development_btn.setOnClickListener(v -> {
            showDialogStages_of_anxiety_development_btn();
        });
        Button elements_of_anxious_thinking_btn = findViewById(R.id.elements_of_anxious_thinking_btn);
        elements_of_anxious_thinking_btn.setOnClickListener(v -> {
            showDialogElements_of_anxious_thinking();
        });
        Button general_worry_btn = findViewById(R.id.general_worry_btn);
        general_worry_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GeneralWorryActivity.class);
            startActivity(intent);
        });
        Button individual_phobias_btn = findViewById(R.id.individual_phobias_btn);
        individual_phobias_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), IndividualPhobiasActivity.class);
            startActivity(intent);
        });
        Button panic_attacks_btn = findViewById(R.id.panic_attacks_btn);
        panic_attacks_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PanicAttacksActivity.class);
            startActivity(intent);
        });
        Button obsessions_btn = findViewById(R.id.obsessions_btn);
        obsessions_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ObsessionsActivity.class);
            startActivity(intent);
        });
        Button hypochondria_btn = findViewById(R.id.hypochondria_btn);
        hypochondria_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HypochondriaActivity.class);
            startActivity(intent);
        });
        Button social_anxiety_btn = findViewById(R.id.social_anxiety_btn);
        social_anxiety_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SocialAnxietyActivity.class);
            startActivity(intent);
        });
    }
    private void showDialogAnxiety_is() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.anxiety_is, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setLayout(600, 400);
        dialog.show();
    }
    private void showDialogStages_of_anxiety_development_btn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.stages_of_anxiety_development, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setLayout(600, 400);
        dialog.show();
    }
    private void showDialogElements_of_anxious_thinking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.elements_of_anxious_thinking, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setLayout(600, 400);
        dialog.show();
    }
    public void onCloseButtonClick(View view) {
        dialog.dismiss();
    }
}