package com.example.face_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CogniTestActivity extends AppCompatActivity {

    private TextView questionTextView, questionCountView, answersListView;
    private SeekBar answerSeekBar;
    private TextView ratingTextView;
    private Button nextButton, infoButton;
    private AlertDialog dialog;

    private float[] userAnswers = new float[45];
    private int currentQuestionIndex = 0, rating;
    private float[] scores = new float[9];
    private String[] choices = {"Никогда", "Иногда", "Часто", "Всегда"};
    private String[] questions = {
            "Я слишком бурно реагирую даже на самые мелкие проблемы.",
            "Мне говорят, что я делаю из мухи слона.",
            "Я легко прихожу в возбуждение.",
            "Не стоит даже пробовать, всё равно ничего не получится.",
            "Я заранее знаю, что всё будет плохо.",
            "Я могу точно сказать, о чем думают другие.",
            "Мои близкие должны знать, чего я хочу.",
            "Всегда можно определить, что думает человек, понаблюдав за его жестами и мимикой.",
            "Я полагаю, что, когда люди проводят много времени вместе, они настраиваются на мысли друг друга.",
            "Я расстраиваюсь из-за того, что, как мне кажется, думает другой человек, а потом оказывается, что я был не прав.",
            "Я в ответе за то, чтобы любимые мною люди были счастливы.",
            "Если что-то не получается, я чувствую, что это моя вина.",
            "Меня критикуют чаще, чем других людей.",
            "Я всегда могу определить, когда человек нападает именно на меня, даже если он не упоминает моего имени.",
            "Я чувствую, что меня несправедливо обвиняют в том, что находится вне моего контроля.",
            "Люди сознательно затрагивают именно те области, в которых я особенно чувствителен к критике.",
            "В отношении критики у меня действует шестое чувство, я всегда угадываю, когда говорят обо мне.",
            "Негативные замечания ранят меня по-настоящему, иногда я впадаю в депрессивное состояние.",
            "Я слышу только негативные замечания и часто не замечаю похвалы.",
            "Я полагаю, что все замечания означают одно и то же, им одна негативная цена.",
            "Я расстраиваюсь, если мне не удается завершить дело.",
            "Если обо мне говорят, что я такой же, как все, или один из многих, я чувствую себя оскорбленным.",
            "Лучше я ничего не буду делать, чем возьмусь за работу ниже моего достоинства.",
            "Для меня очень важно, чтобы люди воспринимали меня как человека, ни на йоту не отступающего от стандарта безупречности.",
            "Даже самая незначительная ошибка может испортить мне весь день и даже всю жизнь.",
            "По сравнению с другими я неудачник.",
            "Во мне силен дух соревнования.",
            "Я расстраиваюсь, когда слышу об успехах других людей.",
            "Я падаю духом оттого, что нахожусь не там, где должен быть.",
            "Мне кажется, что если хочешь добиться успеха, то надо постоянно сравнивать себя с другими.",
            "Мир, знаете ли, очень опасное место.",
            "Если не хочешь иметь неприятности, соблюдай осторожность в словах и делах.",
            "Не люблю пользоваться случаем.",
            "Я упустил хорошую возможность, потому что опасался рисковать.",
            "Я избегаю предпринимать какие-то действия из-за боязни травмы и неудачи.",
            "Я испытываю чувство вины из-за того, что должен был сделать что-то в прошлом, но не сделал.",
            "Я считаю, что надо жить по правилам.",
            "Оглядываясь на прожитую жизнь, я вижу больше неудач, нежели успехов.",
            "На меня давит необходимость поступать правильно.",
            "Меня угнетает необходимость сделать все дела.",
            "Мне безразлично мнение окружающих.",
            "Люди упрекают меня в том, что я не умею слушать.",
            "Когда меня просят что-то сделать, я бываю недовольным, ершистым.",
            "Я считаю, что все должно делаться по-моему или не делаться вовсе.",
            "Я склонен откладывать очень важные дела и бываю очень медлительным."
    };
    public void onCloseButtonClick(View view) {
        dialog.dismiss();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cogni_test);
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_dashboard);
            startActivity(intent);
        });
        rating=1;
        infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.cogni_test_info, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.getWindow().setLayout(600, 400);
            dialog.show();
        });
        questionTextView = findViewById(R.id.questionTextView);
        questionCountView = findViewById(R.id.questionCountView);
        answerSeekBar = findViewById(R.id.answerSeekBar);
        ratingTextView = findViewById(R.id.ratingTextView);
        nextButton = findViewById(R.id.nextButton);
        answersListView = findViewById(R.id.answersListView);
        questionTextView.setText(questions[currentQuestionIndex]);
        ratingTextView.setText(choices[0]);
        answersListView.setText(R.string.answerListCogni);
        questionCountView.setText(String.format(getString(R.string.cogni_test_question_count), currentQuestionIndex + 1));
        answerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rating = progress;
                ratingTextView.setText(choices[rating]);
                rating+=1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswers[currentQuestionIndex] = rating;
                if (currentQuestionIndex < questions.length-1) {
                    currentQuestionIndex++;
                    questionTextView.setText(questions[currentQuestionIndex]);
                    questionCountView.setText(String.format(getString(R.string.cogni_test_question_count), currentQuestionIndex + 1));
                    answerSeekBar.setProgress(0);
                    rating=1;
                } else {
                    calculateRawScores();
                    displayResults();
                }
            }
        });
    }

    private void calculateRawScores() {
        for(int j=0;j<scores.length;j++) {
            for (int i = 0; i < userAnswers.length; i++) {
                switch (j) {
                    case 0:
                        if (isInScale(i, 13, 15, 16, 19, 20)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        break;
                    case 1:
                        if (isInScale(i, 6, 8, 9, 14, 17)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        break;
                    case 2:
                        if (isInScale(i, 10, 11, 12, 21, 39)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        break;
                    case 3:
                        if (isInScale(i, 11, 12, 21, 39, 40)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        break;
                    case 4:
                        if (isInScale(i, 1, 2, 3, 10, 25)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        break;
                    case 5:
                        if (isInScale(i, 4, 5, 26, 28, 29, 35, 36, 38, 44)) {
                            scores[j] += userAnswers[i]/9.0;
                        }
                        break;
                    case 6:
                        if (isInScale(i, 18, 21, 22, 24, 25)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        break;
                    case 7:
                        if (isInScale(i, 9, 23, 31, 33, 34, 35)) {
                            scores[j] += userAnswers[i]/7.0;
                        }
                        if (i==27){
                            scores[j] += (5 - userAnswers[i])/7.0;
                        }
                        break;
                    case 8:
                        if (isInScale(i, 32, 33, 37, 40)) {
                            scores[j] += userAnswers[i]/5.0;
                        }
                        if (i==41){
                            scores[j] += (5 - userAnswers[i])/5.0;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean isInScale(int scaleIndex, int... items) {
        for (int item : items) {
            if (item == scaleIndex) {
                return true;
            }
        }
        return false;
    }

    private void displayResults() {
        Intent intent = new Intent(this, CogniTestResultActivity.class);
        intent.putExtra("Scores", scores);
        startActivity(intent);
    }
}
