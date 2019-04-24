package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.CountDownTimer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView progress_text;
    private ImageView dial;
    private int alcohol_status;
    public long sec = 0;
    public long min = 0;
    public long hour = 0;
    public String timeRemaining = "00:00:00";
    public double BAC = 0;

  //  static double beer = 0.05;
   // static double wine = 0.12;
   // static double liquor = 0.4;

    public int numDrinks = 0;
    public double alcOz = 1.5; //Ounces of drink
    public double alcPercent = 0.4; //Alcohol content of drink

    public int weight = 180; //Get from input, in pounds
    public int gender = 0; //0 for male (genConst = 0.73), 1 for female (genConst = 0.66)
    public double genConst = 0.73;
    public double timeElapsed = 0; //Time elapsed since last sober, in hours
    public String status = "Sober";
    public int dialNum =0;


    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private CountDownTimer mCountDownTimer;
    private static final long START_TIME_IN_MILLIS = 600000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref2 = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        progress_text = findViewById(R.id.textView2);
        progress_text.setText("Sober");

        dial= findViewById(R.id.dial_image);
        dial.setImageResource(R.drawable.dial);
        SharedPreferences.Editor editor = sharedPref2.edit();
        editor.putInt("progress_val", 0);
        editor.putInt("drink_volume", 0);
        editor.putInt("drink_percent", 0);
        editor.commit();
        alcohol_status = 0;

    }

    @Override
    protected void onResume() {
        super.onResume();
       // SharedPreferences sharedPref3 = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        //String progress_val = sharedPref3.getString("progress_val","Didn't work");
       progress_text = findViewById(R.id.textView2);
       progress_text.setText(status);


        SharedPreferences sharedPref3 = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        int progress_val = sharedPref3.getInt("progress_val",0);
        if(progress_val == 1){

            int percent = sharedPref3.getInt("drink_percent",0);
            int volume = sharedPref3.getInt("drink_volume",0);
//lots of drinks
            if(percent * volume > 7500){
                numDrinks += 8;
            }
//medium amount
            else if(percent*volume > 5000 && percent*volume <= 7000){
                numDrinks +=6;
            }
            else if(percent*volume >2500 && percent*volume <= 5000){
                numDrinks+=4;
            }
            else if( percent*volume > 1250 && percent*volume <= 2500){
                numDrinks+=2;
            }
            else if (percent*volume <= 1250){
                numDrinks++;
            }

        }
       if(progress_val == 999){
           numDrinks = 0;
           alcohol_status = 0;
           startTimer();
           weight = sharedPref3.getInt("weight", 180);
           gender = sharedPref3.getInt("gender", 0);
           if(gender == 0) genConst = 0.73;
           else if (gender == 1) genConst = 0.66;
       }


       assessBAC();
       reDrawDial();


       /*if(dialNum ==0){
           dial.setImageResource(R.drawable.dial);
       }
       if(alcohol_status ==1){
           dial.setImageResource(R.drawable.dial2);
       }
        if(alcohol_status ==2){
            dial.setImageResource(R.drawable.dial3);
        }
        if(alcohol_status ==3){
            dial.setImageResource(R.drawable.dial4);
        }
        if(alcohol_status ==4){
            dial.setImageResource(R.drawable.dial5);
        }
        if(alcohol_status ==5){
            dial.setImageResource(R.drawable.dial6);
        }
        if(alcohol_status ==6){
            dial.setImageResource(R.drawable.dial7);
        }
        if(alcohol_status ==7){
            dial.setImageResource(R.drawable.dial8);
        }
        if(alcohol_status ==8){
            dial.setImageResource(R.drawable.dial9);
        }
        if(alcohol_status ==9){
            dial.setImageResource(R.drawable.dial10);
        }*/

       // assessBAC();

       // progress_text.setText(Integer.toString(alcohol_status));
    }


    public void goToSafety(View view)
    {
        Intent intent = new Intent(MainActivity.this, Safety.class);
        startActivity(intent);
    }
    public void goToSettings(View view)
    {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }
    public void goToPlanning(View view)
    {
        Intent intent = new Intent(MainActivity.this, Planning.class);
        startActivity(intent);
    }
    public void goToSocial(View view)
    {
        Intent intent = new Intent(MainActivity.this, Social.class);
        startActivity(intent);
    }
    public void goToAddDrink(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddDrink.class);
        startActivity(intent);
    }
    public void startTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                //assessBAC();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //updateButtons();
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        //progress_text.setText(timeLeftFormatted);
    }

    public void assessBAC(){
        BAC = ((numDrinks*(alcOz*alcPercent)*5.14)/(weight*genConst));
       // BAC -= 0.15*(START_TIME_IN_MILLIS - mTimeLeftInMillis)/1000;

        if(BAC == 0) { status = "Sober"; alcohol_status = 0; }
        else if(BAC > 0 && BAC < 0.06){ status = "Buzzed"; alcohol_status = 1; }
        else if(BAC >= 0.06 && BAC < 0.1){ status = "Tipsy"; alcohol_status = 2; }
        else if(BAC >= 0.1 && BAC < 0.13){ status = "Slightly Drunk"; alcohol_status = 3; }
        else if(BAC >= 0.13 && BAC < 0.16){ status = "Moderately Drunk"; alcohol_status = 5; }
        else if(BAC >= 0.16 && BAC < 0.2){ status = "Extremely Drunk"; alcohol_status = 7; }
        else if(BAC >= 0.2 && BAC < 0.25){ status = "Wasted"; alcohol_status = 8; }
        else if(BAC >= 0.25 && BAC < 0.4){ status = "Alcohol Poisoning"; alcohol_status = 9; }
        else if(BAC >= 0.4) status = "R.I.P";
        else if(BAC >= 1.0) status = "Blood is Pure Alcohol";

        progress_text.setText(status);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
       // updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
               // updateButtons();
            } else {
                startTimer();
            }
        }
    }

    public void reDrawDial(){
        if(alcohol_status ==0){
            dial.setImageResource(R.drawable.dial);
        }
        if(alcohol_status ==1){
            dial.setImageResource(R.drawable.dial2);
        }
        if(alcohol_status==2){
            dial.setImageResource(R.drawable.dial3);
        }
        if(alcohol_status==3){
            dial.setImageResource(R.drawable.dial4);
        }
        if(alcohol_status==4){
            dial.setImageResource(R.drawable.dial5);
        }
        if(alcohol_status==5){
            dial.setImageResource(R.drawable.dial6);
        }
        if(alcohol_status==6){
            dial.setImageResource(R.drawable.dial7);
        }
        if(alcohol_status ==7){
            dial.setImageResource(R.drawable.dial8);
        }
        if(alcohol_status==8){
            dial.setImageResource(R.drawable.dial9);
        }
        if(alcohol_status ==9){
            dial.setImageResource(R.drawable.dial10);
        }

    }
}


