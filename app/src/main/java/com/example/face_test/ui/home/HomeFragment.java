package com.example.face_test.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.face_test.R;
import com.example.face_test.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private int progressValue=10;
    private List<Integer> cardsList;
    private List<Integer> selectedCards;
    private View root;
//    private int selectUniquePicture(int numCards) {
//        Random random = new Random();
//        selectedCards.clear();
//        while (selectedCards.size() < numCards) {
//            int randomIndex = random.nextInt(cardsList.size());
//            int cardId = cardsList.get(randomIndex);
//            if (!selectedCards.contains(cardId)) {
//                return cardId;
//            }
//        }
//        return 0;
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        cardsList = new ArrayList<>();
        selectedCards = new ArrayList<>();
        ImageSlider imageSlider = root.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            slideModels.add(new SlideModel(R.drawable.morning, ScaleTypes.FIT));
        } else if (hour >= 12 && hour < 18) {
            slideModels.add(new SlideModel(R.drawable.afternoon, ScaleTypes.FIT));
        } else {
            slideModels.add(new SlideModel(R.drawable.evening, ScaleTypes.FIT));
        }
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();
        while (uniqueNumbers.size() < 4) {
            int randomIndex = random.nextInt(12)+1;
            uniqueNumbers.add(randomIndex);
        }
        for (Integer randomIndex : uniqueNumbers) {
            int resourceId = getResources().getIdentifier("picture" + randomIndex, "drawable", requireContext().getPackageName());
            slideModels.add(new SlideModel(resourceId, ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        SeekBar seekBar = root.findViewById(R.id.seekBar);
        TextView mood_palette_result = root.findViewById(R.id.mood_palette_result);
        Button mood_palette_btn = root.findViewById(R.id.mood_palette_btn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 10) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_1));
                    progressValue = 10;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                } else if (progress <= 20) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_2));
                    progressValue = 20;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                } else if (progress <= 30) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_3));
                    progressValue = 30;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                } else if (progress <= 40) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_4));
                    progressValue = 40;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                } else if (progress <= 50) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_5));
                    progressValue = 50;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                } else if (progress <= 60) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_6));
                    progressValue = 60;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                } else if (progress <= 70) {
                    seekBar.setProgressDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.seekbar_progress_7));
                    progressValue = 70;
                    mood_palette_btn.setVisibility(View.VISIBLE);
                    mood_palette_result.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mood_palette_btn.setOnClickListener(v -> {
            if(progressValue!=0) {
                mood_palette_btn.setVisibility(View.INVISIBLE);
                if(progressValue == 10){
                    mood_palette_result.setText("Уныние");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.brown));
                } else if(progressValue == 20) {
                    mood_palette_result.setText("Тревога");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.purple));
                } else if(progressValue == 30) {
                    mood_palette_result.setText("Грусть");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                } else if(progressValue == 40) {
                    mood_palette_result.setText("Спокойствие");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                } else if(progressValue == 50) {
                    mood_palette_result.setText("Хорошее");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
                } else if(progressValue == 60) {
                    mood_palette_result.setText("Радость");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.orange));
                } else if(progressValue == 70) {
                    mood_palette_result.setText("Восторг");
                    mood_palette_result.setVisibility(View.VISIBLE);
                    mood_palette_result.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                }
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