package com.example.face_test;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CogniTestResultActivity extends AppCompatActivity {
    private String[] questions = {
            "Персонализация",
            "Чтение мыслей",
            "Упрямство",
            "Морализация",
            "Катастрофизация",
            "Выученная беспомощность",
            "Максимализм",
            "Преувеличение опасности",
            "Гипернормативность"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cogni_test_result);
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("selectedTab", R.id.navigation_dashboard);
            startActivity(intent);
        });
        float[] scores = getIntent().getFloatArrayExtra("Scores");
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        ProgressBar personalizationProgressBar = findViewById(R.id.progressBar1);
        ProgressBar mind_readingProgressBar = findViewById(R.id.progressBar2);
        ProgressBar stubbornnessProgressBar = findViewById(R.id.progressBar3);
        ProgressBar moralizationProgressBar = findViewById(R.id.progressBar4);
        ProgressBar catastrophizationProgressBar = findViewById(R.id.progressBar5);
        ProgressBar learned_helplessnessProgressBar = findViewById(R.id.progressBar6);
        ProgressBar maximalismProgressBar = findViewById(R.id.progressBar7);
        ProgressBar exaggeratingProgressBar = findViewById(R.id.progressBar8);
        ProgressBar hypernormalityProgressBar = findViewById(R.id.progressBar9);
        TextView testResultText = findViewById(R.id.testResultText);
        String resultText="";
        int redColor = getResources().getColor(R.color.red);
        int greenColor = getResources().getColor(R.color.green);
        for (int i = 0; i < scores.length; i++) {
            TableRow row = new TableRow(CogniTestResultActivity.this);
            TextView scaleTextView = new TextView(this);
            scaleTextView.setText(questions[i]);
            TextView rawScoreTextView = new TextView(this);
            rawScoreTextView.setText(String.valueOf(scores[i]));
            rawScoreTextView.setGravity(Gravity.CENTER);
            row.addView(scaleTextView);
            row.addView(rawScoreTextView);
            tableLayout.addView(row);
        }
        for(int j=0;j<scores.length;j++) {
            int maxValue = 10;
            int progress = (int) (scores[j] * maxValue);
            switch (j) {
                case 0:
                    personalizationProgressBar.setProgress(progress);
                    break;
                case 1:
                    mind_readingProgressBar.setProgress(progress);
                    break;
                case 2:
                    stubbornnessProgressBar.setProgress(progress);
                    break;
                case 3:
                    moralizationProgressBar.setProgress(progress);
                    break;
                case 4:
                    catastrophizationProgressBar.setProgress(progress);
                    break;
                case 5:
                    learned_helplessnessProgressBar.setProgress(progress);
                    break;
                case 6:
                    maximalismProgressBar.setProgress(progress);
                    break;
                case 7:
                    exaggeratingProgressBar.setProgress(progress);
                    break;
                case 8:
                    hypernormalityProgressBar.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
        int progress1 = personalizationProgressBar.getProgress();
        int progress2 = mind_readingProgressBar.getProgress();
        int progress3 = stubbornnessProgressBar.getProgress();
        int progress4 = moralizationProgressBar.getProgress();
        int progress5 = catastrophizationProgressBar.getProgress();
        int progress6 = learned_helplessnessProgressBar.getProgress();
        int progress7 = maximalismProgressBar.getProgress();
        int progress8 = exaggeratingProgressBar.getProgress();
        int progress9 = hypernormalityProgressBar.getProgress();
        if (progress1 >= 1 && progress1 <= 12) {
            personalizationProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Персонализация» (предполагая, что пользователь склонен игнорировать замечания и мнения окружающих):</b><br/>" +
                    "1.&emsp;Откройтесь для обратной связи: постарайтесь быть более открытым к замечаниям и критике со стороны других людей. Иногда критика может помочь вам увидеть свои слабые стороны и работать над ними.<br/>" +
                    "2.&emsp;Проявляйте эмпатию: постарайтесь понять точку зрения других людей и их мотивы, прежде чем реагировать на их замечания. Это поможет вам лучше контролировать свои эмоции и не воспринимать их как личную атаку.<br/>" +
                    "3.&emsp;Развивайте самоанализ: проводите время на саморефлексию и анализ своих действий и поведения. Это поможет вам лучше понять себя и свои реакции на мнения окружающих.<br/>";
        } else {
            personalizationProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Персонализация» (предполагая, что пользователь считает, что окружающие относятся к нему враждебно или неодобрительно):</b><br/>" +
                    "1.&emsp;Развивайте навыки коммуникации: улучшите свои навыки общения, чтобы лучше понимать интонации и намерения других людей. Это может помочь вам не воспринимать нейтральные замечания как критику или отвержение.<br/>" +
                    "2.&emsp;Работайте над самопринятием: поверьте в себя и свои способности. Чем сильнее ваша уверенность в себе, тем меньше вы будете воспринимать замечания окружающих как критику.<br/>" +
                    "3.&emsp;Практикуйте позитивное мышление: уделяйте больше внимания позитивным аспектам своей жизни и своей личности. Это поможет вам видеть мир более оптимистично и не реагировать слишком сильно на критику.<br/>";
        }
        if (progress2 >= 1 && progress2 <= 17) {
            mind_readingProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Чтение мыслей» (когда человек имеет тенденцию не обращать внимание на мысли и намерения других):</b><br/>" +
                    "1.&emsp;Улучшайте коммуникацию: старайтесь лучше понимать других людей, задавайте им вопросы и активно слушайте их ответы. Это поможет вам лучше понять их мысли и намерения.<br/>" +
                    "2.&emsp;Обращайте внимание на невербальные сигналы: учите себя читать невербальные сигналы других людей, такие как мимика, жесты и тон голоса. Они могут дать вам дополнительную информацию о их мыслях и эмоциях.<br/>" +
                    "3.&emsp;Будьте внимательны к контексту: учитывайте обстоятельства и контекст ситуации при оценке мыслей и намерений других людей. Иногда поведение может быть обусловлено внешними факторами, которые необходимо учитывать.<br/>";
        } else {
            mind_readingProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Чтение мыслей» (когда человек склонен делать нелогические выводы и додумывать за других):</b><br/>" +
                    "1.&emsp;Проверяйте свои предположения: прежде чем делать выводы о мыслях и намерениях других людей, удостоверьтесь, что у вас достаточно доказательств для этого. Постарайтесь представить себя на месте другого человека и подумайте, какие могут быть альтернативные объяснения его поведения.<br/>" +
                    "2.&emsp;Развивайте эмпатию: старайтесь понимать чувства и мотивы других людей, ставьте себя на их место. Это поможет вам видеть ситуацию с разных точек зрения и избегать односторонних суждений.<br/>" +
                    "3.&emsp;Практикуйте логическое мышление: обращайте внимание на логику и объективные данные, а не только на свои субъективные ощущения. Учите себя анализировать информацию и делать выводы на основе фактов, а не предположений.<br/>";
        }
        if (progress3 >= 1 && progress3 <= 23) {
            stubbornnessProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Упрямство» (когда человек склонен к согласию с мнением других и не отстаивает свою самооценку из страха ошибиться):</b><br/>" +
                    "1.&emsp;Укрепляйте свою уверенность: развивайте веру в себя и свои способности. Помните, что каждый человек имеет право на собственное мнение и часто ошибается. Ошибки - это возможность для роста и самосовершенствования.<br/>" +
                    "2.&emsp;Практикуйте аргументацию: учитесь выражать свои мысли и мнения четко и уверенно, даже если они расходятся с мнением других. Это поможет вам защищать свои интересы и быть более убедительным в обсуждениях и спорах.<br/>" +
                    "3.&emsp;Будьте открыты к конструктивной критике: принимайте обратную связь от других людей как возможность для улучшения себя и своих действий. Постарайтесь извлечь уроки из ошибок и применить их в будущем.<br/>";
        } else {
            stubbornnessProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Упрямство» (когда человек настойчиво отстаивает свою самооценку и склонен к эгоцентрической иерархизации и сужению проблемного поля):</b><br/>" +
                    "1.&emsp;Практикуйте открытость к различным точкам зрения: старайтесь понимать и уважать мнения и предложения других людей, даже если они расходятся с вашими. Будьте готовы рассмотреть различные аргументы и варианты решения проблемы.<br/>" +
                    "2.&emsp;Развивайте гибкость мышления: постарайтесь расширить свое проблемное поле и рассмотреть ситуацию с различных точек зрения. Помните, что в мире существует множество разнообразных подходов к решению проблем, и нередко самый эффективный из них не совпадает с вашим собственным.<br/>" +
                    "3.&emsp;Учитесь контролировать свои эмоции: осознавайте свои эмоциональные реакции и старайтесь не допускать, чтобы они мешали вашему рациональному мышлению. Практикуйте методы релаксации и управления стрессом, чтобы сохранять спокойствие и ясность ума во время спорных ситуаций.<br/>";
        }
        if (progress4 >= 1 && progress4 <= 25) {
            moralizationProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Морализация» (когда человек не проявляет высокой моральной ответственности и не стремится к контролю над окружающими через моральные нормы):</b><br/>" +
                    "1.&emsp;Обратите внимание на свои ценности: проведите время, чтобы проанализировать свои моральные установки и ценности. Размышляйте о том, что для вас важно и какие принципы руководят вашим поведением.<br/>" +
                    "2.&emsp;Практикуйте этику взаимодействия: старайтесь действовать согласно принципам справедливости, честности и уважения к другим людям. Это поможет вам развивать свою моральность и чувство ответственности перед окружающими.<br/>" +
                    "3.&emsp;Изучите моральные аспекты различных ситуаций: при обсуждении различных вопросов и проблем, задумайтесь о моральных аспектах и последствиях различных решений. Это поможет вам лучше понимать свои моральные установки и становиться более ответственным в своих действиях.<br/>";
        } else {
            moralizationProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Морализация» (когда человек декларирует повышенную моральную ответственность и стремится к обеспечению безопасности через моральный контроль над окружающими):</b><br/>" +
                    "1.&emsp;Поддерживайте свои убеждения, но будьте терпимы: важно придерживаться своих моральных принципов, но также уважать право других людей на собственные убеждения. Не забывайте о ценности толерантности и открытости к различным точкам зрения.<br/>" +
                    "2.&emsp;Развивайте эмпатию и понимание: старайтесь поставить себя на место других людей и понять их мотивы и переживания. Это поможет вам быть более гибким и понимающим в общении с окружающими.<br/>" +
                    "3.&emsp;Обратите внимание на контекст: помните, что моральные нормы и ценности могут различаться в разных культурах и средах. Учитывайте особенности контекста, прежде чем делать суждения о поведении других людей.<br/>";
        }
        if (progress5 >= 1 && progress5 <= 17) {
            catastrophizationProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Катастрофизация» (когда человек не склонен преувеличивать значимость проблем и реагировать на них слишком эмоционально):</b><br/>" +
                    "1.&emsp;Не игнорируйте реальные проблемы: важно не погружаться в безразличие к проблемам, даже если они кажутся незначительными. Обращайте внимание на свои эмоции и реагируйте на них адекватно.<br/>" +
                    "2.&emsp;Развивайте навыки решения проблем: научитесь анализировать проблемы, искать рациональные решения и действовать на их основе. Это поможет вам более эффективно справляться с трудностями и избегать излишней тревожности.<br/>" +
                    "3.&emsp;Общайтесь с доверенными лицами: если у вас возникают затруднения в оценке ситуаций, обсудите их с доверенными друзьями или специалистами, которые могут помочь вам посмотреть на проблемы с разных точек зрения и дать объективную оценку.<br/>";
        } else {
            catastrophizationProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Катастрофизация» (когда человек склонен преувеличивать значимость проблем и реагировать на них слишком эмоционально):</b><br/>" +
                    "1.&emsp;Оцените реальные риски: попробуйте рационально оценить ситуацию и реальные угрозы, связанные с проблемой. Часто мы склонны видеть опасность вещей, которые на самом деле не так страшны. Обратите внимание на факты и данные, которые могут помочь вам более объективно взглянуть на ситуацию.<br/>" +
                    "2.&emsp;Научитесь управлять эмоциями: изучите методы релаксации и техники управления стрессом, такие как глубокое дыхание, медитация или йога. Это поможет вам снизить уровень тревожности и более спокойно реагировать на проблемы.<br/>" +
                    "3.&emsp;Избегайте перфекционизма: постарайтесь осознать, что никто не совершенен, и ошибки - это естественная часть жизни. Позвольте себе быть неидеальным и принимать свои ошибки как возможность для роста и улучшения.<br/>";
        }
        if (progress6 >= 1 && progress6 <= 16) {
            learned_helplessnessProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Выученная беспомощность» (когда человек не склонен обесценивать себя и свои возможности):</b><br/>" +
                    "1.&emsp;Поддерживать позитивное самовосприятие: продолжать признавать и ценить свои достижения и качества.<br/>" +
                    "2.&emsp;Развивать адаптивные стратегии решения проблем: активно преодолевать затруднения и искать конструктивные пути достижения целей.<br/>" +
                    "3.&emsp;Продолжать работать над самосовершенствованием: стремиться к личностному росту и развитию, а не принимать статус как данность.<br/>" +
                    "4.&emsp;Стремиться к лидерству: использовать свои возможности и способности для влияния на окружающих и достижения высоких результатов.<br/>";
        } else {
            learned_helplessnessProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Выученная беспомощность» (когда человек склонен обесценивать себя и свои возможности):</b><br/>" +
                    "1.&emsp;Повышать осознание собственной ценности: помогать себе осознавать свои достижения, качества и способности. Это может включать в себя ведение дневника достижений или ежедневное упражнение на поиск положительных сторон в себе.<br/>" +
                    "2.&emsp;Развивать навыки самоуважения: учиться уважать себя таким, какой вы есть, и относиться к себе с добротой и состраданием. Это может включать в себя практику самосострадания и самоуважения.<br/>" +
                    "3.&emsp;Работать над установкой реалистичных целей: помогать себе установить достижимые цели и шаги к их достижению. Это может помочь переоценить свои способности и возможности.<br/>" +
                    "4.&emsp;Избегать самообвинений: помогать себе понять, что неудачи не всегда зависят от личных недостатков, и не зацикливаться на самокритике.<br/>";
        }
        if (progress7 >= 1 && progress7 <= 17) {
            maximalismProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Максимализм» (когда человек не склонен к амбициозности и крайностям в оценках):</b><br/>" +
                    "1.&emsp;Стремиться к личностному росту: устанавливать амбициозные, но реалистичные цели и стремиться к их достижению.<br/>" +
                    "2.&emsp;Избегать самообесценивания: учиться видеть и ценить свои достижения и усилия, даже если они кажутся небольшими или незначительными.<br/>" +
                    "3.&emsp;Развивать уверенность в себе: работать над укреплением собственной самооценки и веры в свои способности.<br/>" +
                    "4.&emsp;Изучать причины недооценки: понять, почему возникает чувство недооценки и что может быть сделано для преодоления этого чувства.<br/>";
        } else {
            maximalismProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Максимализм» (когда человек склонен к амбициозности, крайности в оценках и потребности в восхищении):</b><br/>" +
                    "1.&emsp;Развивать реалистичное самовосприятие: помогать себе видеть себя объективно и осознавать свои сильные и слабые стороны без преувеличений.<br/>" +
                    "2.&emsp;Объективно оценивать свои достижения: научиться оценивать свои успехи с учетом реальности, избегая преувеличений и переоценки своих заслуг.<br/>" +
                    "3.&emsp;Принимать критику конструктивно: научиться видеть конструктивную критику как возможность для роста и улучшения, а не как угрозу для своего самооценки.<br/>" +
                    "4.&emsp;Развивать эмпатию: стараться понимать и уважать точки зрения других людей, не обесценивая их мнения и усилия.<br/>";
        }
        if (progress8 >= 1 && progress8 <= 19) {
            exaggeratingProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Преувеличение опасности» (когда человек не склонен к переоценке опасностей и избеганию рисков):</b><br/>" +
                    "1.&emsp;Обеспечивать безопасность: помнить о возможных опасностях, но не позволять им препятствовать нормальной жизнедеятельности и самореализации.<br/>" +
                    "2.&emsp;Принимать риски с умом: выявлять риски и принимать их с учетом реальных обстоятельств и возможных последствий, но не избегать их излишне из-за страха.<br/>" +
                    "3.&emsp;Развивать уверенность: повышать свою уверенность в себе и своих способностях, чтобы не бояться испытывать новые ситуации и принимать решения.<br/>" +
                    "4.&emsp;Избегать бездумных решений: не пренебрегать осторожностью и разумным анализом рисков, чтобы избежать ненужных опасностей.<br/>";
        } else {
            exaggeratingProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Преувеличение опасности» (когда человек склонен к уклонению от рисков из-за переоценки опасностей):</b><br/>" +
                    "1.&emsp;Развивать реалистичное восприятие опасности: помогать себе оценивать риски и опасности объективно, исходя из фактов, а не из чувств и предположений.<br/>" +
                    "2.&emsp;Преодолевать страхи постепенно: постепенно выходить за пределы зоны комфорта и преодолевать страхи, начиная с небольших и безопасных шагов.<br/>" +
                    "3.&emsp;Развивать стратегии преодоления препятствий: научиться разрабатывать планы действий для преодоления препятствий и решения проблем, вместо ухода от них из-за страха.<br/>" +
                    "4.&emsp;Обращаться за поддержкой: обсуждать свои опасения с близкими или профессионалами, чтобы получить поддержку и советы по преодолению страхов.<br/>";
        }
        if (progress9 >= 1 && progress9 <= 25) {
            hypernormalityProgressBar.setProgressTintList(ColorStateList.valueOf(redColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Гипернормативность» (когда человек не слишком сильно придает значения социальным нормам и не стремится обезопасить себя за счет строгого следования им):</b><br/>" +
                    "1.&emsp;Учитывать социальные конвенции: помнить о социальных нормах и правилах, чтобы не нарушать общепринятые правила поведения и не создавать ненужных конфликтов.<br/>" +
                    "2.&emsp;Развивать эмпатию: учитывать чувства и потребности других людей и стараться соблюдать социальные нормы для поддержания гармоничных отношений.<br/>" +
                    "3.&emsp;Стремиться к адаптации: научиться адаптироваться к различным социальным ситуациям, сохраняя при этом собственную индивидуальность и автономию.<br/>" +
                    "4.&emsp;Соблюдать баланс: находить золотую середину между соблюдением социальных норм и выражением собственной уникальности, не теряя при этом аутентичности.<br/>";
        } else {
            hypernormalityProgressBar.setProgressTintList(ColorStateList.valueOf(greenColor));
            resultText+="•&emsp;<b>Рекомендации для шкалы «Гипернормативность» (когда человек слишком сильно отождествляет себя с социальными нормами и стремится обезопасить себя за счет строгого следования им):</b><br/>" +
                    "1.&emsp;Развивать гибкость мышления: научиться рассматривать ситуации с разных точек зрения и осознавать, что не все правила всегда должны быть абсолютными.<br/>" +
                    "2.&emsp;Принимать свою уникальность: осознать, что каждый человек уникален, и не стоит стремиться к полному соответствию стандартам, которые могут быть неприменимы в конкретных ситуациях.<br/>" +
                    "3.&emsp;Развивать самоосознание: осознавать свои собственные ценности, потребности и желания, вместо того чтобы ориентироваться исключительно на внешние социальные нормы.<br/>" +
                    "4.&emsp;Проявлять инициативу: избегать чрезмерной зависимости от мнения окружающих и развивать способность к самостоятельному принятию решений.<br/>";
        }
        testResultText.setText(Html.fromHtml(resultText));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String uid = mAuth.getCurrentUser().getUid();
            String currentTime = String.valueOf(System.currentTimeMillis());
            String path = "users_emotions/" + uid + "/cogniTest/"+currentTime;
            List<Float> rawScoresList = new ArrayList<>();
            for(int i=0; i<scores.length; i++){
                rawScoresList.add(scores[i]);
            }
            Map<String, Object> emotionsMap = new HashMap<>();
            emotionsMap.put("scores", rawScoresList);
            DatabaseReference userEmotionsRef = database.getReference(path);
            userEmotionsRef.setValue(emotionsMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Данные успешно сохранены в базе данных Firebase.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Ошибка при сохранении данных в базе данных Firebase.", e);
                        }
                    });
        }
    }
}