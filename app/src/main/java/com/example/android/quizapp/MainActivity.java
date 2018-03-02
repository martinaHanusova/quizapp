package com.example.android.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int rightAnswers = 0;
    private int questionNumber = 0;
    private List<Question> questions = new ArrayList<>();
    private RelativeLayout contentLayout;
    private LinearLayout linearLayout;
    private ProgressBar progressB;
    private TextView instruction;
    private Button sendAnswer;
    private Button reset;
    private TextView rightAnswersText;
    private TextView wrongAnswersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.question_layout);
        contentLayout = findViewById(R.id.content_layout);
        progressB = findViewById(R.id.progressB);
        rightAnswersText = findViewById(R.id.right_answer);
        wrongAnswersText = findViewById(R.id.wrong_answer);
        instruction = findViewById(R.id.instruction);
        reset = findViewById(R.id.reset);
        sendAnswer = findViewById(R.id.send_answer);

        String[][] answers =  new String[][]{
                new String[]{getString(R.string.Czech_Republic), getString(R.string.Panama), getString(R.string.Barbados), getString(R.string.Iceland)},
                new String[]{getString(R.string.Jordan), getString(R.string.Kenya), getString(R.string.Poland), getString(R.string.Czech_Republic)},
                new String[]{getString(R.string.Latvia), getString(R.string.Togo), getString(R.string.Jamaica), getString(R.string.Syria)},
                new String[]{getString(R.string.Chile), getString(R.string.Italy), getString(R.string.Mauritius), getString(R.string.Nepal)},
                new String[]{getString(R.string.Corsica), getString(R.string.Bhutan), getString(R.string.Pakistan), getString(R.string.Chad)},
                new String[]{getString(R.string.Kazakhstan), getString(R.string.Uruguay), getString(R.string.Albania), getString(R.string.Barbados)},
                new String[]{getString(R.string.Iran), getString(R.string.Haiti), getString(R.string.Norway), getString(R.string.Czech_Republic)},
                new String[]{getString(R.string.Norway), getString(R.string.Belgium), getString(R.string.Finland), getString(R.string.Denmark)},
                new String[]{getString(R.string.Colombia), getString(R.string.Greece), getString(R.string.Peru), getString(R.string.Uganda)},
                new String[]{getString(R.string.Jordan), getString(R.string.Tonga), getString(R.string.Tajikistan), getString(R.string.India)},
        };

        // Creating images for questions
        for (int i = 0; i < 10; i++) {
            ImageView flag = new ImageView(this);
            flag.setImageResource(getResources().getIdentifier("img" + i, "drawable", getPackageName()));

        // Creating questions for quiz
            Question otazka = new Question(flag, answers[i], indexRightAnswers[i], this);
            questions.add(otazka);
            Collections.shuffle(questions);
        }

        // Seting first question
        linearLayout.addView(questions.get(questionNumber).getLayout());

        sendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    public void reset() {
        for (int i = 0; i <= questionNumber; i++) {
            questions.get(i).clearCheck();
        }
        linearLayout.removeView(questions.get(questionNumber).getLayout());
        if (questionNumber >= 9) {
            instruction.setText(getString(R.string.this_flag_belongs_to_which_country));
            contentLayout.removeView(rightAnswersText);
            contentLayout.addView(linearLayout);
            sendAnswer.setVisibility(View.VISIBLE);
            rightAnswersText.setText(null);
            wrongAnswersText.setText(null);
        }
        questionNumber = 0;
        rightAnswers = 0;
        progressB.setProgress(10);
        Collections.shuffle(questions);
        linearLayout.addView(questions.get(questionNumber).getLayout());
    }

    public void nextQuestion() {
        if (questions.get(questionNumber).evaluateQuestion()) {
            rightAnswers += 1;
        }
        if (questionNumber < 9) {
            linearLayout.removeView(questions.get(questionNumber).getLayout());
            questionNumber += 1;
            linearLayout.addView(questions.get(questionNumber).getLayout());
            progressB.incrementProgressBy(10);
        } else {
            contentLayout.removeView(progressB);
            contentLayout.removeView(linearLayout);
            sendAnswer.setVisibility(View.GONE);
            instruction.setText(getString(R.string.results));
            rightAnswersText.setText(getString(R.string.right_answers, rightAnswers));
            wrongAnswersText.setText(getString(R.string.wrong_answers, 10 - rightAnswers));
        }
    }

    // index of right answer
    private int[] indexRightAnswers = new int[]{1, 3, 1, 0, 2, 0, 1, 2, 3, 3};
}

