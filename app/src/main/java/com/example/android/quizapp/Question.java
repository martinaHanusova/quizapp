package com.example.android.quizapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinahanusova on 28.02.18.
 */

public class Question {
    public enum TypeOfAnswer {RADIO_BUTTONS, CHECK_BOX, EDIT_TEXT}
    private TypeOfAnswer typeOfAnswer;
    private ImageView image;
    private String[] answers;
    private int[] rightAnswers;
    private Context mContext;
    private LinearLayout questionLayout;
    private RadioGroup radioGroup;
    private List<CheckBox> checkboxGroup;
    private EditText editAnswer;

    /**
     * @param image        image question
     * @param answers      array of answers
     * @param rightAnswers index of right answer
     * @param typeOfAnswer if its radio/checkbox or editText
     * @param mContext
     */
    Question(ImageView image, String[] answers, int[] rightAnswers, TypeOfAnswer typeOfAnswer, Context mContext) {
        this.image = image;
        this.answers = answers;
        this.rightAnswers = rightAnswers;
        this.typeOfAnswer = typeOfAnswer;
        this.mContext = mContext;

    }

    /**
     * Method for setting a question
     *
     * @return a layout  which contains question and answers
     */
    public LinearLayout getLayout() {
        if (questionLayout == null) {
            this.questionLayout = new LinearLayout(mContext);
            questionLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            image.setLayoutParams(lp);
            questionLayout.addView(image);
            appendAnswers(questionLayout);
        }
        return questionLayout;

    }

    /**
     * Method for create answers for a question
     *
     * @return layout with answers
     */
    private void appendAnswers(LinearLayout layout) {
        if (typeOfAnswer == TypeOfAnswer.RADIO_BUTTONS) {
            radioGroup = new RadioGroup(mContext);
            for (int i = 0; i < answers.length; i++) {
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(answers[i]);
                radioGroup.addView(radioButton);
            }
            layout.addView(radioGroup);
        } else if (typeOfAnswer == TypeOfAnswer.CHECK_BOX) {
            checkboxGroup = new ArrayList<>();
            for (int i = 0; i < answers.length; i++) {
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setId(i);
                checkBox.setText(answers[i]);
                checkboxGroup.add(checkBox);
                layout.addView(checkBox);
            }
        } else {
            editAnswer = new EditText(mContext);
            layout.addView(editAnswer);
        }

    }

    /**
     * Method for evaluating question
     *
     * @return true if answer is right
     */
    public boolean evaluateQuestion() {
        if (typeOfAnswer == TypeOfAnswer.RADIO_BUTTONS) {
            int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
            return checkedRadioButton == rightAnswers[0];
        } else if (typeOfAnswer == TypeOfAnswer.CHECK_BOX) {
            int numberOfRightAnswers = 0;
            for (int i = 0; i < checkboxGroup.size(); i++) {
                    for (int j = 0; j < rightAnswers.length; j++) {
                    if (checkboxGroup.get(i).isChecked() && checkboxGroup.get(i).getId() == rightAnswers[j]) {
                        numberOfRightAnswers += 1;
                    }
                }
            }
            return numberOfRightAnswers == rightAnswers.length;
        } else {
            String answer = editAnswer.getText().toString();
            return answer.equalsIgnoreCase(answers[0]);
        }
    }

    /**
     * Method for clearing
     */
    public void clearCheck() {
        if (typeOfAnswer == TypeOfAnswer.RADIO_BUTTONS) {
            radioGroup.clearCheck();
        } else if (typeOfAnswer == TypeOfAnswer.CHECK_BOX) {
            for (int i = 0; i < checkboxGroup.size(); i++) {
                checkboxGroup.get(i).setChecked(false);
            }
        } else {
            editAnswer.setText(null);
        }
    }

    /**
     * Method for getting type of answer
     * @return type of answer
     */

    public TypeOfAnswer getTypeOfAnswer() {
        return typeOfAnswer;
    }
}
