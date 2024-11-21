package com.example.face_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardsActivity extends AppCompatActivity {
    private List<Integer> cardsList;
    private List<Integer> selectedCards;
    private List<SlideModel> slideModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_dashboard);
            startActivity(intent);
        });
        cardsList = new ArrayList<>();
        selectedCards = new ArrayList<>();
        slideModel = new ArrayList<>();
        cardsList.add(R.drawable.card1);
        cardsList.add(R.drawable.card2);
        cardsList.add(R.drawable.card3);
        cardsList.add(R.drawable.card4);
        cardsList.add(R.drawable.card5);
        cardsList.add(R.drawable.card6);
        cardsList.add(R.drawable.card7);
        cardsList.add(R.drawable.card8);
        cardsList.add(R.drawable.card9);
        cardsList.add(R.drawable.card10);
        selectUniqueCards(5);
        for (int cardId : selectedCards) {
            SlideModel slideModel = new SlideModel(cardId, ScaleTypes.FIT);
            this.slideModel.add(slideModel);
        }
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        imageSlider.setImageList(slideModel, ScaleTypes.FIT);

    }
    private void selectUniqueCards(int numCards) {
        Random random = new Random();
        selectedCards.clear();
        while (selectedCards.size() < numCards) {
            int randomIndex = random.nextInt(cardsList.size());
            int cardId = cardsList.get(randomIndex);
            if (!selectedCards.contains(cardId)) {
                selectedCards.add(cardId);
            }
        }
    }
}