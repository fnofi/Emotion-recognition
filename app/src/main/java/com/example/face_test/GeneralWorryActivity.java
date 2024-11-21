package com.example.face_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GeneralWorryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_worry);
        Button intro_btn = findViewById(R.id.intro_btn);
        Button signs_btn = findViewById(R.id.signs_btn);
        Button tips_btn = findViewById(R.id.tips_btn);
        Button button_close = findViewById(R.id.button_close);
        TextView definition = findViewById(R.id.definition);
        TextView sings = findViewById(R.id.general_sings);
        TextView tips1 = findViewById(R.id.general_tips1);
        TextView tips2 = findViewById(R.id.general_tips2);
        TextView textView_note = findViewById(R.id.textView_note);
        ImageView general_worry_img = findViewById(R.id.generalWorry_img);
        intro_btn.setOnClickListener(v -> {
            definition.setVisibility(View.VISIBLE);
            general_worry_img.setVisibility(View.VISIBLE);
            sings.setVisibility(View.GONE);
            tips1.setVisibility(View.GONE);
            tips2.setVisibility(View.GONE);
            textView_note.setVisibility(View.GONE);
        });
        signs_btn.setOnClickListener(v -> {
            definition.setVisibility(View.GONE);
            general_worry_img.setVisibility(View.GONE);
            sings.setVisibility(View.VISIBLE);
            tips1.setVisibility(View.GONE);
            tips2.setVisibility(View.GONE);
            textView_note.setVisibility(View.GONE);
        });
        tips_btn.setOnClickListener(v -> {
            definition.setVisibility(View.GONE);
            general_worry_img.setVisibility(View.GONE);
            sings.setVisibility(View.GONE);
            tips1.setVisibility(View.VISIBLE);
            tips2.setVisibility(View.VISIBLE);
            textView_note.setVisibility(View.VISIBLE);
        });
        button_close.setOnClickListener(v -> {
            finish();
        });
    }
}