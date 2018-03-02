package com.example.android.quizapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by martinahanusova on 28.02.18.
 */

public class Question {
    private ImageView image;
    private String[] answers;
    private int rightAnswer;
    private Context mContext;
    private LinearLayout questionLayout;
    private RadioGroup answersGroup;

    /**
     *
     * @param image image question
     * @param answers array of answers
     * @param rightAnswer index of right answer
     * @param mContext
     */
    Question(ImageView image, String[] answers, int rightAnswer, Context mContext) {
        this.image = image;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
        this.mContext = mContext;

    }

    /**
     * Method for setting a question
     * @return a layout  which contains question and answers
     */
    public LinearLayout getLayout() {
        if (this.questionLayout == null) {
            this.questionLayout = new LinearLayout(mContext);
            questionLayout.setOrientation(LinearLayout.VERTICAL);
            this.answersGroup = new RadioGroup(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            image.setLayoutParams(lp);
            questionLayout.addView(image);
            questionLayout.addView(createAnswersRadios());
        }
        return questionLayout;

    }

    /**
     * Method for create a radioGroup with answers
     * @return radioGroup which contains answers
     */
    private RadioGroup createAnswersRadios() {
        for (int i = 0; i < answers.length; i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setId(i);
            radioButton.setText(answers[i]);
            answersGroup.addView(radioButton);
        }
        return answersGroup;

    }

    /**
     * Method for evaluating question
     * @return true if checked answer is right
     */
    public boolean evaluateQuestion() {
        int checkedRadioButton = answersGroup.getCheckedRadioButtonId();
        if (checkedRadioButton == rightAnswer) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method for clear checked radioButton
     */
    public void clearCheck() {
        answersGroup.clearCheck();
    }
}
