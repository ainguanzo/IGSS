package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.view.Window;
import android.view.View;


public class Planning extends AppCompatActivity {

    public EditText weightField;
    public EditText timerField;
    public RadioGroup genderGroup;
    public RadioButton maleButton;
    public RadioButton femaleButton;

    private static final int male_ID = 100;//first radio button id
    private static final int female_ID = 101;//first radio button id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_planning);
        genderGroup = (RadioGroup)findViewById(R.id.genderRadio);
        maleButton = (RadioButton)findViewById(R.id.radioButton);
        femaleButton = (RadioButton)findViewById(R.id.radioButton2);
        maleButton.setId(male_ID);
        femaleButton.setId(female_ID);
    }


    public void startNewSession(View view){
        timerField = (EditText)findViewById(R.id.timerText);
        weightField = (EditText)findViewById(R.id.weightText);
        int timerValue = Integer.parseInt(timerField.getText().toString());
        int weightValue = Integer.parseInt(weightField.getText().toString());
        int gender = genderGroup.getCheckedRadioButtonId();
        if(gender == 100) gender = 0;
        else if(gender == 101) gender = 1;


        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val", 999);
        editor.putInt("timer_length", timerValue);
        editor.putInt("gender", gender);
        editor.putInt("weight", weightValue);
        editor.commit();

        close(view);
    }

    public void close(View view){
        Intent openMainActivity= new Intent(Planning.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }
}
