package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Safety extends AppCompatActivity {

    private TextView countdownText;

    private Button resetButton;
    private Button pauseButton;
    private Button alertPoliceButton;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;

    private long millisUntilFinished = 1800000; /* 30 minutes */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        millisUntilFinished = sharedPref.getLong("millisLeft", 1800000);

        countdownText = findViewById(R.id.countdownText);
        resetButton = findViewById(R.id.resetButton);
        pauseButton = findViewById(R.id.pauseButton);
        alertPoliceButton = findViewById(R.id.alertPoliceButton);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        alertPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:911"));
                startActivity(intent);
            }
        });


    }

    public void resetTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        millisUntilFinished = sharedPref.getLong("millisLeft", 1800000);
        updateTime();
    }

    public void startStop(){
        if(timerRunning) stopTimer();
        else startTimer();
    }

    public void stopTimer(){
        countDownTimer.cancel();
        pauseButton.setText("START");
        timerRunning = false;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long l) {
                millisUntilFinished = l;
                updateTime();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        pauseButton.setText("STOP");
        timerRunning = true;
    }

    public void updateTime(){
        int minutes = (int) millisUntilFinished / 60000;
        int seconds = (int) millisUntilFinished % 60000 / 1000;

        String timeLeft;
        timeLeft = "" + minutes;
        timeLeft += ":";

        if(seconds < 10) timeLeft += "0";

        timeLeft += seconds;

        countdownText.setText(timeLeft);
    }

    public void goHome(View view)
    {

        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val",0);
        editor.putLong("millisLeft", millisUntilFinished);
        editor.commit();

        Intent openMainActivity= new Intent(Safety.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }

    public void goToAddDrink(View view)
    {
        Intent intent = new Intent(Safety.this, AddDrink.class);
        startActivity(intent);
    }
}
