package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ImageButton button;


    private static final long START_TIME_IN_MILLIS = 1800000;

    private TextView mTextViewCountDown;
    private Button mButtonEnableDisable;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning = true;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    public void goToAddDrink(View view)
    {
        Intent intent = new Intent(Settings.this, AddDrink.class);
        startActivity(intent);
    }

    public void goHome(View view)
    {

        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val",0);
        editor.commit();

        Intent openMainActivity= new Intent(Settings.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mTextViewCountDown = findViewById(R.id.timer);

        mButtonEnableDisable = findViewById(R.id.disablebutton);
        mButtonReset = findViewById(R.id.resetbutton);

        mButtonEnableDisable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mTimerRunning){
                    resetTimerDisabled();
                }
                else{
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetTimer();
            }
        });

        if (mTimerRunning){
            startTimer();
        }


        updateCountDownText();



    }


    private void startTimer(){
        SharedPreferences sharedPref2 = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        int minutesLeft = sharedPref2.getInt("timer_length", 30);
        mCountDownTimer = new CountDownTimer((minutesLeft*60000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonEnableDisable.setText("Enable");
                mButtonEnableDisable.setVisibility(View.INVISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonEnableDisable.setText("Disable");


    }


    private void resetTimer(){
        if (mTimerRunning) {
            mCountDownTimer.cancel();
            mTimerRunning = false;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            updateCountDownText();
            startTimer();
        }

    }

    private void resetTimerDisabled(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonEnableDisable.setText("Enable");
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}