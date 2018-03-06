package com.example.android.quizapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private int rightAnswers = 0;
    private int questionNumber = 0;
    private ArrayList<Question> questions = new ArrayList<>();
    private LinearLayout linearLayout;
    private ProgressBar progressB;
    private TextView instruction;
    private Button sendAnswer;
    private Button reset;
    private Toast toast;
    private String message;
    private Activity activity;
    private int value;
    private Question.TypeOfOrientation orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        linearLayout = findViewById(R.id.question_layout);
        progressB = findViewById(R.id.progressB);
        instruction = findViewById(R.id.textview_instruction);
        reset = findViewById(R.id.btn_reset);
        sendAnswer = findViewById(R.id.btn_send_answer);
        value = activity.getResources().getConfiguration().orientation;

        questions = new ArrayList<>();

        String[][] answers = new String[][]{
                new String[]{getString(R.string.Czech_Republic), getString(R.string.Panama), getString(R.string.Barbados), getString(R.string.Iceland)},
                new String[]{getString(R.string.Jordan), getString(R.string.Kenya), getString(R.string.Poland), getString(R.string.Czech_Republic)},
                new String[]{getString(R.string.Finland), getString(R.string.Slovenia), getString(R.string.India), getString(R.string.Russia)},
                new String[]{getString(R.string.Laos)},
                new String[]{getString(R.string.Poland), getString(R.string.Finland), getString(R.string.Chad), getString(R.string.Romania)},
                new String[]{getString(R.string.Italy)},
                new String[]{getString(R.string.Iraq), getString(R.string.Kazakhstan), getString(R.string.Yemen), getString(R.string.Syria)},
                new String[]{getString(R.string.Japan)},
                new String[]{getString(R.string.Latvia), getString(R.string.Togo), getString(R.string.Jamaica), getString(R.string.Syria)},
                new String[]{getString(R.string.Chile), getString(R.string.Italy), getString(R.string.Mauritius), getString(R.string.Nepal)},
                new String[]{getString(R.string.Corsica), getString(R.string.Bhutan), getString(R.string.Pakistan), getString(R.string.Chad)},
                new String[]{getString(R.string.Kazakhstan), getString(R.string.Uruguay), getString(R.string.Albania), getString(R.string.Barbados)},
                new String[]{getString(R.string.Iran), getString(R.string.Haiti), getString(R.string.Norway), getString(R.string.Czech_Republic)},
                new String[]{getString(R.string.Norway), getString(R.string.Belgium), getString(R.string.Finland), getString(R.string.Denmark)},
                new String[]{getString(R.string.Colombia), getString(R.string.Greece), getString(R.string.Peru), getString(R.string.Uganda)},
                new String[]{getString(R.string.Jordan), getString(R.string.Tonga), getString(R.string.Tajikistan), getString(R.string.India)},

        };

        if (value == Configuration.ORIENTATION_PORTRAIT) {
            orientation = Question.TypeOfOrientation.VERTICAL;
        }

        if (value == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = Question.TypeOfOrientation.HORIZONTAL;
        }

        if (savedInstanceState != null) {

            rightAnswers = savedInstanceState.getInt("rightAnswers");
            questionNumber = savedInstanceState.getInt("questionNumber");
            questions = savedInstanceState.getParcelableArrayList("questions");

        } else {
            for (int i = 0; i < answers.length; i++) {
                // Creating questions for quiz
                Question question = new Question("img" + i, answers[i], indexRightAnswers[i], questionType[i]);
                questions.add(i, question);
            }
            Collections.shuffle(questions);
        }

        // Setting first question
        setInstruction();
        linearLayout.removeAllViews();
        linearLayout.addView(questions.get(questionNumber).getLayout(orientation, this));


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

    // save values
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rightAnswers", rightAnswers);
        outState.putInt("questionNumber", questionNumber);
        outState.putParcelableArrayList("questions", questions);
    }

    // Set instruction for questions
    private void setInstruction() {
        if (questions.get(questionNumber).getTypeOfAnswer() == Question.TypeOfAnswer.CHECK_BOX) {
            instruction.setText(getString(R.string.which_state_has_similar_flag));
        } else {
            instruction.setText(getString(R.string.this_flag_belongs_to_which_country));
        }
    }

    // reset score and set new questions
    private void reset() {
        for (int i = 0; i < questionNumber; i++) {
            questions.get(i).clearCheck();
        }
        linearLayout.removeAllViews();
        questionNumber = 0;
        rightAnswers = 0;
        progressB.setProgress(10);
        // Collections.shuffle(questions);
        setInstruction();
        Collections.shuffle(questions);
        linearLayout.addView(questions.get(questionNumber).getLayout(orientation, this));
    }

    // set next question
    private void nextQuestion() {
        System.out.println(rightAnswers);
        System.out.println(questionNumber);
        if (questions.get(questionNumber).evaluateQuestion()) {
            rightAnswers += 1;
        }
        if (questionNumber < 9) {
            linearLayout.removeAllViews();
            questionNumber += 1;
            linearLayout.addView(questions.get(questionNumber).getLayout(orientation, this));
            progressB.incrementProgressBy(10);
            setInstruction();
        } else {
            String comment = "";
            if (rightAnswers < 4) {
                comment += getString(R.string.so_so);
            } else if (rightAnswers < 7) {
                comment += getString(R.string.not_too_bad);
            } else if (rightAnswers < 10) {
                comment += getString(R.string.great_score);
            } else {
                comment += getString(R.string.ten_out_of_ten);
            }
            message = comment + "\n" + getString(R.string.right_answers, rightAnswers) + "\n" + getString(R.string.wrong_answers, 10 - rightAnswers);
            toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
            reset();
        }
    }

    // index of right answer
    private int[][] indexRightAnswers = new int[][]{
            new int[]{1},
            new int[]{3},
            new int[]{1, 3},
            new int[]{0},
            new int[]{2, 3},
            new int[]{0},
            new int[]{0, 2, 3},
            new int[]{0},
            new int[]{1},
            new int[]{0},
            new int[]{2},
            new int[]{0},
            new int[]{1},
            new int[]{2},
            new int[]{3},
            new int[]{3},
    };


    // type of answer of question
    private Question.TypeOfAnswer[] questionType = new Question.TypeOfAnswer[]{
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.CHECK_BOX,
            Question.TypeOfAnswer.EDIT_TEXT,
            Question.TypeOfAnswer.CHECK_BOX,
            Question.TypeOfAnswer.EDIT_TEXT,
            Question.TypeOfAnswer.CHECK_BOX,
            Question.TypeOfAnswer.EDIT_TEXT,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
            Question.TypeOfAnswer.RADIO_BUTTONS,
    };
}


