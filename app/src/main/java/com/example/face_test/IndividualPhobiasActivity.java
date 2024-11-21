package com.example.face_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class IndividualPhobiasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_phobias);
        TextView individual_phobias_definition = findViewById(R.id.definition);
        ImageView individual_phobias_image = findViewById(R.id.individual_phobias_image);
        TextView individual_phobias_sings = findViewById(R.id.individual_phobias_sings);
        TextView individual_phobias_tips1 = findViewById(R.id.individual_phobias_tips1);
        TextView individual_phobias_tips2 = findViewById(R.id.individual_phobias_tips2);
        TextView textView_note = findViewById(R.id.textView_note);
        Button button_close=findViewById(R.id.button_close);
        button_close.setOnClickListener(v -> {
            finish();
        });
        Button intro_btn=findViewById(R.id.intro_btn);
        intro_btn.setOnClickListener(v -> {
            individual_phobias_definition.setVisibility(View.VISIBLE);
            individual_phobias_image.setVisibility(View.VISIBLE);
            individual_phobias_sings.setVisibility(View.GONE);
            individual_phobias_tips1.setVisibility(View.GONE);
            individual_phobias_tips2.setVisibility(View.GONE);
            textView_note.setVisibility(View.GONE);
        });
        Button signs_btn=findViewById(R.id.signs_btn);
        signs_btn.setOnClickListener(v -> {
            individual_phobias_definition.setVisibility(View.GONE);
            individual_phobias_image.setVisibility(View.GONE);
            individual_phobias_sings.setVisibility(View.VISIBLE);
            individual_phobias_tips1.setVisibility(View.GONE);
            individual_phobias_tips2.setVisibility(View.GONE);
            textView_note.setVisibility(View.GONE);
        });
        Button tips_btn=findViewById(R.id.tips_btn);
        tips_btn.setOnClickListener(v -> {
            individual_phobias_definition.setVisibility(View.GONE);
            individual_phobias_image.setVisibility(View.GONE);
            individual_phobias_sings.setVisibility(View.GONE);
            individual_phobias_tips1.setVisibility(View.VISIBLE);
            individual_phobias_tips2.setVisibility(View.VISIBLE);
            textView_note.setVisibility(View.VISIBLE);
        });
    }
}