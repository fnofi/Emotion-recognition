package com.example.face_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HypochondriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypochondria);
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(v -> {
            finish();
        });
        Button intro_btn = findViewById(R.id.intro_btn);
        Button signs_btn = findViewById(R.id.signs_btn);
        Button tips_btn = findViewById(R.id.tips_btn);
        TextView definition = findViewById(R.id.definition);
        TextView sings = findViewById(R.id.sings);
        TextView tips1 = findViewById(R.id.tips1);
        TextView tips2 = findViewById(R.id.tips2);
        TextView textView_note = findViewById(R.id.textView_note);
        ImageView img = findViewById(R.id.img);
        intro_btn.setOnClickListener(v -> {
            definition.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
            sings.setVisibility(View.GONE);
            tips1.setVisibility(View.GONE);
            tips2.setVisibility(View.GONE);
            textView_note.setVisibility(View.GONE);
        });
        signs_btn.setOnClickListener(v -> {
            definition.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            sings.setVisibility(View.VISIBLE);
            tips1.setVisibility(View.GONE);
            tips2.setVisibility(View.GONE);
            textView_note.setVisibility(View.GONE);
        });
        tips_btn.setOnClickListener(v -> {
            definition.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            sings.setVisibility(View.GONE);
            tips1.setVisibility(View.VISIBLE);
            tips2.setVisibility(View.VISIBLE);
            textView_note.setVisibility(View.VISIBLE);
        });
    }
}