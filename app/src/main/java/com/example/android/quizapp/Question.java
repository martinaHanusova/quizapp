package com.example.android.quizapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
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

public class Question implements Parcelable {

    protected Question(Parcel in) {
        image = in.readString();
        answers = in.createStringArray();
        rightAnswers = in.createIntArray();
        typeOfAnswer = TypeOfAnswer.values()[in.readInt()];
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeStringArray(answers);
        parcel.writeIntArray(rightAnswers);
        parcel.writeInt(typeOfAnswer.ordinal());
    }

    public enum TypeOfAnswer {RADIO_BUTTONS, CHECK_BOX, EDIT_TEXT}

    public enum TypeOfOrientation {HORIZONTAL, VERTICAL}

    private TypeOfAnswer typeOfAnswer;
    private String image;
    private String[] answers;
    private int[] rightAnswers;
    private LinearLayout questionLayout;
    private RadioGroup radioGroup;
    private List<CheckBox> checkboxGroup;
    private EditText editAnswer;


    /**
     * @param image        image question
     * @param answers      array of answers
     * @param rightAnswers index of right answer
     * @param typeOfAnswer if its radio/checkbox or editText
     */
    Question(String image, String[] answers, int[] rightAnswers, TypeOfAnswer typeOfAnswer) {
        this.image = image;
        this.answers = answers;
        this.rightAnswers = rightAnswers;
        this.typeOfAnswer = typeOfAnswer;

    }

    /**
     * Method for setting a question
     *
     * @return a layout  which contains question and answers
     */
    public LinearLayout getLayout(TypeOfOrientation orientation, Context mContext) {
        if (questionLayout == null || questionLayout.getContext() != mContext) {
            this.questionLayout = new LinearLayout(mContext);
            if (orientation == TypeOfOrientation.VERTICAL) {
                questionLayout.setOrientation(LinearLayout.VERTICAL);
            } else {
                questionLayout.setOrientation(LinearLayout.HORIZONTAL);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            ImageView image = new ImageView(mContext);
            image.setImageResource(mContext.getResources().getIdentifier(this.image, "drawable", mContext.getPackageName()));
            image.setLayoutParams(lp);
            questionLayout.addView(image);
            appendAnswers(questionLayout, mContext);
        }

        return questionLayout;

    }

    /**
     * Method for create answers for a question
     *
     * @return layout with answers
     */
    private void appendAnswers(LinearLayout layout, Context mContext) {
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
            LinearLayout checkboxLayout = new LinearLayout(mContext);
            checkboxLayout.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < answers.length; i++) {
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setId(i);
                checkBox.setText(answers[i]);
                checkboxGroup.add(checkBox);
                checkboxLayout.addView(checkBox);

            }
            layout.addView(checkboxLayout);
        } else {
            editAnswer = new EditText(mContext);
            editAnswer.setMinEms(10);
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
            int numberOfCheckedAnswers = 0;
            for (int i = 0; i < checkboxGroup.size(); i++) {
                if (checkboxGroup.get(i).isChecked()) {
                    numberOfCheckedAnswers += 1;
                }
                for (int j = 0; j < rightAnswers.length; j++) {
                    if (checkboxGroup.get(i).isChecked() && checkboxGroup.get(i).getId() == rightAnswers[j]) {
                        numberOfRightAnswers += 1;
                    }
                }
            }
            return numberOfRightAnswers == rightAnswers.length && numberOfRightAnswers == numberOfCheckedAnswers;
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
            if (radioGroup != null) {
                radioGroup.clearCheck();
            }
        } else if (typeOfAnswer == TypeOfAnswer.CHECK_BOX) {
            if (checkboxGroup != null) {
                for (int i = 0; i < checkboxGroup.size(); i++) {
                    checkboxGroup.get(i).setChecked(false);
                }
            }
        } else {
            if (editAnswer != null) {
                editAnswer.setText(null);
            }
        }
    }

    /**
     * Method for getting type of answer
     *
     * @return type of answer
     */

    public TypeOfAnswer getTypeOfAnswer() {
        return typeOfAnswer;
    }
}
