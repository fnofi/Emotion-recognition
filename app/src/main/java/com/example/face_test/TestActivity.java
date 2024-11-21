package com.example.face_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private TextView questionTextView, questionCountView, genderText, answersListView;
    private SeekBar answerSeekBar;
    private TextView ratingTextView;
    private AlertDialog dialog;
    private Button nextButton, maleButton, femaleButton, infoButton;

    private int[] userAnswers = new int[57];
    private int currentQuestionIndex = 0, rating;

    private int[] rawScores = new int[8];
    private int[] tScores = new int[8];
    private boolean gender;

    private static int[] standardScoresAK = {};

    private static int[] standardScoresBO = {};

    private static int[] standardScoresTO = {};

    private static int[] standardScoresPA = {};

    private static int[] standardScoresCP = {};
    private static int[] standardScoresYC = {};

    private static int[] standardScoresYD = {};

    private static int[] standardScoresPO = {};
    private String[] choices = {};
    private String[] questions = {
            "Мой организм сильно реагирует на перепады погоды или изменения климата.",
            "Очень часто бывает настроение, когда я легко отвлекаюсь от дела, становлюсь рассеянным и мечтательным.",
            "Я во всем стараюсь быть первым и лучшим.",
            "Я чувствую себя в большом нервном напряжении.",
            "Я тревожусь очень часто.",
            "Я безосновательно чувствую себя довольно несчастным.",
            "Я часто плохо засыпаю.",
            "Повседневные трудности лишают меня покоя.",
            "У меня очень часто бывают периоды такого сильного беспокойства, что мне трудно усидеть на месте.",
            "Мои мысли постоянно возвращаются к возможным неудачам, и мне трудно направить их в другое русло.",
            "Я часто хвалю людей, которых знаю очень мало.",
            "Я испытываю определенное беспокойство, боязнь сам не знаю отчего.",
            "Если все против меня, я нисколько не падаю духом.",
            "Я просыпаюсь утром неотдохнувшим и усталым.",
            "Часто я чувствую себя бесполезным.",
            "Мне не удается сдерживать свою досаду или гнев.",
            "Трудные задачи у меня поднимают настроение.",
            "У меня часто болит голова.",
            "Часто бывает, что я с кем-то посплетничаю.",
            "Масса мелких неприятностей выводит меня из себя.",
            "У меня часто возникает предчувствие, что меня ожидает какое-то наказание.",
            "Меня легко задеть словом.",
            "Я полон энергии.",
            "Очень часто какой-нибудь пустяк овладевает моими мыслями и беспокоит меня несколько дней.",
            "Я начинаю нервничать, когда задумываюсь обо всем, что меня ожидает.",
            "Существует конфликт между моими планами и действительностью.",
            "Очень часто я чувствую себя усталым, вялым.",
            "В ситуациях длительных нервно-психических нагрузок я проявляю выносливость.",
            "Часто из-за кого-нибудь я теряю самообладание.",
            "Я склонен преувеличивать в своих мыслях негативное отношение ко мне близких людей.",
            "У меня очень часто бывает тяжесть в голове.",
            "Мои манеры за столом дома обычно не так хороши, как в гостях.",
            "Я упускаю удобный случай часто из-за того, что недостаточно быстро принимаю решение.",
            "Есть очень много вещей, которые меня легко раздражают.",
            "Я часто испытываю чувство напряжения и беспокойства, думая о происшедшем в течение дня.",
            "Когда я неважно себя чувствую, я раздражительный.",
            "Из-за волнения у меня часто пропадает сон.",
            "Меня можно назвать человеком, полным надежд.",
            "Совсем незначительные препятствия меня сильно раздражают.",
            "Очень часто у меня бывает хандра (тоскливое настроение).",
            "Я ощущаю, что мне не хватает времени, чтобы сделать всё, что нужно.",
            "Мне часто приходят в голову нехорошие мысли, о которых лучше не рассказывать.",
            "Утром, после пробуждения, я еще долго чувствую себя усталым и разбитым.",
            "Мне очень нравится постоянно преодолевать новые трудности.",
            "У меня есть желание изменить в своем образе жизни очень многое, но не хватает сил.",
            "В большинстве случаев я легко преодолеваю разочарование.",
            "Удовлетворение одних моих потребностей и желаний делает невозможным удовлетворение других.",
            "У меня очень большая выносливость к умственной работе.",
            "Часто неприличная или даже непристойная шутка меня смешит.",
            "Я смотрю в будущее с полной уверенностью.",
            "Я очень часто теряю терпение.",
            "Люди разочаровывают меня.",
            "Мне все быстро надоедает.",
            "Мне кажется, что я близок к нервному срыву.",
            "Я часто испытываю общую слабость.",
            "Мне часто говорят, что я вспыльчив.",
            "Меня утомляют люди."
    };
    public void onCloseButtonClick(View view) {
        dialog.dismiss();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button button_close = findViewById(R.id.button_close);
        maleButton = findViewById(R.id.maleButton);
        femaleButton = findViewById(R.id.femaleButton);
        infoButton=findViewById(R.id.infoButton);
        button_close.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_dashboard);
            startActivity(intent);
        });
        infoButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.emotion_test_info, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.getWindow().setLayout(600, 400);
            dialog.show();
        });
        rating=1;
        questionTextView = findViewById(R.id.questionTextView);
        questionCountView = findViewById(R.id.questionCountView);
        answerSeekBar = findViewById(R.id.answerSeekBar);
        ratingTextView = findViewById(R.id.ratingTextView);
        nextButton = findViewById(R.id.nextButton);
        genderText = findViewById(R.id.genderText);
        answersListView = findViewById(R.id.answersListView);
        questionTextView.setText(questions[currentQuestionIndex]);
        questionCountView.setText(String.format(getString(R.string.test_question_count), currentQuestionIndex + 1));
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
                    questionCountView.setText(String.format(getString(R.string.test_question_count), currentQuestionIndex + 1));
                    answerSeekBar.setProgress(0);
                    rating=1;
                } else {
                    calculateRawScores();
                    if(gender)
                        convertRawScoresToTScoresFemale();
                    else
                        convertRawScoresToTScores();
                    displayResults();
                }
            }
        });
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender=false;
                standardScoresAK = new int[]{
                        24, 25, 26, 28, 29, 30, 32, 33, 34, 36, 37, 38, 40, 41, 42, 44, 45, 46, 48, 49, 50, 52, 53, 54, 56, 57, 58, 60, 61, 62, 64, 65, 66, 68, 69, 70, 72, 73, 74, 76
                };
                standardScoresBO = new int[]{
                        75, 73, 71, 70, 68, 66, 65, 63, 61, 60, 58, 56, 55, 53, 51, 50, 48, 46, 45, 43, 41, 40, 38, 36, 35, 33, 31, 30, 28, 26, 25, 23
                };
                standardScoresTO = new int[]{
                        66, 64, 63, 62, 61, 59, 58, 57, 56, 54, 53, 52, 50, 49, 48, 47, 45, 44, 43, 41, 40, 39, 38, 36, 35, 34, 32, 31, 30, 29, 27, 26, 25, 23
                };
                standardScoresPA = new int[]{
                        70,68, 67, 66, 64, 63, 62,61,59,57,56,54,53,52,50,49,47,46,45,43,42,40,39,38,36,35,33,32,31,29,28,26,25,24
                };
                standardScoresCP = new int[]{
                        66,65,64,63,61,60,59,58,57,56,55,53,52,51,50,49,48,47,45,44,43,42,41,40,39,37,36,35,34,33,32,30,29,28,27,26,25,24
                };
                standardScoresYC = new int[]{
                        67,66,65,64,63,61,60,59,58,57,56,55,53,52,51,50,49,48,47,45,44,43,42,41,40,39,37,36,35,34,33,32,30,29,28,27,26,25,24
                };
                standardScoresYD = new int[]{
                        75,74,73,72,71,70,68,67,66,65,64,63,62,61,59,58,57,56,55,54,53,52,50,49,48,47,46,45,44,43,41,40,39,38,37,36,35,34,32,31,30,29,28,27,26,25,23
                };
                standardScoresPO = new int[]{
                        74,72,70,69,67,65,64,62,60,58,57,55,53,52,50,48,47,45,43,42,40,38,36,35,33,31,30,28,26,25,23
                };
                choices = new String[]{"Полностью не согласен", "Согласен в малой степени", "Согласен почти наполовину", "Согласен наполовину", "Согласен более чем наполовину", "Согласен почти полностью", "Согласен полностью"};
                genderText.setVisibility(View.GONE);
                maleButton.setVisibility(View.GONE);
                infoButton.setVisibility(View.GONE);
                femaleButton.setVisibility(View.GONE);
                questionCountView.setVisibility(View.VISIBLE);
                questionTextView.setVisibility(View.VISIBLE);
                answerSeekBar.setVisibility(View.VISIBLE);
                ratingTextView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                answersListView.setVisibility(View.VISIBLE);
                ratingTextView.setText(choices[0]);
                answersListView.setText(R.string.answerListMale);
            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender=true;
                standardScoresAK = new int[]{
                        23,25,26,27,28,29,30,32,33,34,35,36,38,39,40,41,42,44,45,46,47,48,50,51,52,53,54,55,57,58,59,60,61,63,64,65,66,67,69,70,71,72,73,75,76,77
                };
                standardScoresBO = new int[]{
                        74,73,72,70,69,67,66,65,63,62,60,59,58,56,55,53,52,51,49,48,47,45,44,42,41,40,38,37,35,34,33,31,30,28,27,26,24
                };
                standardScoresTO = new int[]{
                        68,67,66,65,64,63,62,61,60,59,58,57,55,54,53,52,51,50,49,48,47,46,45,44,43,42,40,39,38,37,36,35,34,33,32,31,30,29,28,26,25,24
                };
                standardScoresPA = new int[]{
                        71,69,68,67,66,65,63,62,61,60,59,58,56,55,54,53,52,50,49,48,47,46,44,43,42,41,40,38,37,36,35,34,33,31,30,29,28,27,25,24
                };
                standardScoresCP = new int[]{
                        68,67,66,65,64,63,62,61,60,59,58,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24
                };
                standardScoresYC = new int[]{
                        70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,37,36,35,34,33,32,31,30,29,28,27,26,25,24
                };
                standardScoresYD = new int[]{
                        77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,61,60,59,58,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,36,35,34,33,32,31,30,29,28,27,26,25
                };
                standardScoresPO = new int[]{
                        75,73,72,70,68,67,65,63,62,60,59,57,55,54,52,50,49,47,46,44,42,41,39,38,36,34,33,31,29,28,26,25,23
                };
                choices = new String[]{"Полностью не согласна", "Согласна в малой степени", "Согласна почти наполовину", "Согласна наполовину", "Согласна более чем наполовину", "Согласна почти полностью", "Согласна полностью"};
                genderText.setVisibility(View.GONE);
                maleButton.setVisibility(View.GONE);
                infoButton.setVisibility(View.GONE);
                femaleButton.setVisibility(View.GONE);
                questionCountView.setVisibility(View.VISIBLE);
                questionTextView.setVisibility(View.VISIBLE);
                answerSeekBar.setVisibility(View.VISIBLE);
                ratingTextView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                answersListView.setVisibility(View.VISIBLE);
                ratingTextView.setText(choices[0]);
                answersListView.setText(R.string.answerListFemale);
            }
        });
    }

    private void calculateRawScores() {
        for(int j=0;j<rawScores.length;j++) {
            for (int i = 0; i < userAnswers.length; i++) {
                    switch (j) {
                    case 0:
                        if (isInScale(i, 12, 16, 22, 27, 43, 45, 47, 49)) {
                            rawScores[j] += userAnswers[i];

                        }
                        break;
                    case 1:
                        if (isInScale(i, 5, 7, 38)) {
                            rawScores[j] += userAnswers[i];
                        }
                        if (isInScale(i, 2, 16, 37, 49)) {
                            rawScores[j] += 8 - userAnswers[i];
                        }
                        break;
                    case 2:
                        if (isInScale(i, 0, 6, 13, 17, 26, 42, 54, 56)) {
                            rawScores[j] += userAnswers[i];
                        }
                        break;
                    case 3:
                        if (isInScale(i, 3, 7, 24, 30, 34, 36, 40)) {
                            rawScores[j] += userAnswers[i];
                        }
                        break;
                    case 4:
                        if (isInScale(i, 4, 8, 9, 11, 20, 23, 29, 34)) {
                            rawScores[j] += userAnswers[i];
                        }
                        break;
                    case 5:
                        if (isInScale(i, 15, 19, 28, 33, 50, 52, 53, 55)) {
                            rawScores[j] += userAnswers[i];
                        }
                        break;
                    case 6:
                        if (isInScale(i, 1, 14, 21, 25, 32, 39, 44, 46, 51)) {
                            rawScores[j] += userAnswers[i];
                        }
                        break;
                    case 7:
                        if (isInScale(i, 10, 18, 31, 35, 41, 48)) {
                            rawScores[j] += userAnswers[i];
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

    private void convertRawScoresToTScores() {
        for(int j=0;j<rawScores.length;j++) {
            switch (j) {
                case 0:
                    if(rawScores[j]<17)
                        tScores[j]=24;
                    else if (rawScores[j]>56) {
                        tScores[j]=76;
                    } else
                        tScores[j] = standardScoresAK[rawScores[j]-17];
                    break;
                case 1:
                    if(rawScores[j]<7)
                        tScores[j]=75;
                    else if (rawScores[j]>38)
                        tScores[j] = 23;
                    else
                        tScores[j] = standardScoresBO[rawScores[j]-7];
                    break;
                case 2:
                    if(rawScores[j]<8)
                        tScores[j]=66;
                    else if(rawScores[j]>41)
                        tScores[j]=23;
                    else
                        tScores[j] = standardScoresTO[rawScores[j]-8];
                    break;
                case 3:
                    if(rawScores[j]<7)
                        tScores[j]=70;
                    else if(rawScores[j]>40)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresPA[rawScores[j]-7];
                    break;
                case 4:

                    if(rawScores[j]<8)
                        tScores[j]=66;
                    else if(rawScores[j]>45)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresCP[rawScores[j]-8];
                    break;
                case 5:
                    if(rawScores[j]<8)
                        tScores[j]=67;
                    else if(rawScores[j]>46)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresYC[rawScores[j]-8];
                    break;
                case 6:
                    if(rawScores[j]<9)
                        tScores[j]=75;
                    else if(rawScores[j]>55)
                        tScores[j]=23;
                    else
                        tScores[j] = standardScoresYD[rawScores[j]-9];
                    break;
                case 7:
                    if(rawScores[j]<6)
                        tScores[j]=74;
                    else if(rawScores[j]>36)
                        tScores[j]=23;
                    else
                        tScores[j] = standardScoresPO[rawScores[j]-6];
                    break;
                default:
                    break;
            }
        }
    }
    private void convertRawScoresToTScoresFemale() {
        for(int j=0;j<rawScores.length;j++) {
            switch (j) {
                case 0:
                    if(rawScores[j]<11)
                        tScores[j]=23;
                    else if (rawScores[j]>56) {
                        tScores[j]=77;
                    } else
                        tScores[j] = standardScoresAK[rawScores[j]-11];
                    break;
                case 1:
                    if(rawScores[j]<7)
                        tScores[j]=74;
                    else if (rawScores[j]>43)
                        tScores[j] = 24;
                    else
                        tScores[j] = standardScoresBO[rawScores[j]-7];
                    break;
                case 2:
                    if(rawScores[j]<8)
                        tScores[j]=68;
                    else if(rawScores[j]>49)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresTO[rawScores[j]-8];
                    break;
                case 3:
                    if(rawScores[j]<7)
                        tScores[j]=71;
                    else if(rawScores[j]>46)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresPA[rawScores[j]-7];
                    break;
                case 4:

                    if(rawScores[j]<8)
                        tScores[j]=68;
                    else if(rawScores[j]>52)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresCP[rawScores[j]-8];
                    break;
                case 5:
                    if(rawScores[j]<8)
                        tScores[j]=70;
                    else if(rawScores[j]>52)
                        tScores[j]=24;
                    else
                        tScores[j] = standardScoresYC[rawScores[j]-8];
                    break;
                case 6:
                    if(rawScores[j]<9)
                        tScores[j]=77;
                    else if(rawScores[j]>59)
                        tScores[j]=25;
                    else
                        tScores[j] = standardScoresYD[rawScores[j]-9];
                    break;
                case 7:
                    if(rawScores[j]<6)
                        tScores[j]=75;
                    else if(rawScores[j]>38)
                        tScores[j]=23;
                    else
                        tScores[j] = standardScoresPO[rawScores[j]-6];
                    break;
                default:
                    break;
            }
        }
    }
    private void displayResults() {
        Intent intent = new Intent(this, TestResultActivity.class);
        intent.putExtra("rawScores", rawScores);
        intent.putExtra("tScores", tScores);
        startActivity(intent);
    }
}
