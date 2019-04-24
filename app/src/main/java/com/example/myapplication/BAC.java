package com.example.myapplication;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BAC extends AppCompatActivity {

    Button button1, button2; // button1: start timer; button2: add drink
    TextView textView; //time remaining
    TextView textViewBAC; //intoxication status

    //Unused at the moment, default added drink is 1.5 oz of 40% liquor (1 "standard" drink)
    static double beer = 0.05;
    static double wine = 0.12;
    static double liquor = 0.4;

    public int numDrinks = 0;
    public double alcOz = 1.5; //Ounces of drink
    public double alcPercent = 0.4; //Alcohol content of drink

    public int weight = 180; //Get from input, in pounds
    public int gender = 0; //0 for male (genConst = 0.73), 1 for female (genConst = 0.66)
    public double genConst = 0.73;
    public double timeElapsed = 0; //Time elapsed since last sober, in hours

    public int dialNum = 1; //# corresponding to which dial image to use
    public double BAC = 0;
    // BAC = ((alcOz*alcPercent)*5.14)/(weight*genConst) - 0.15*(timeElapsed)

    public String status = "Sober";

    public long sec = 0;
    public long min = 0;
    public long hour = 0;

    public String timeRemaining = "00:00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bac);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        textViewBAC = (TextView) findViewById(R.id.textViewBAC);

        textView.setText(timeRemaining);
        textViewBAC.setText(status);


        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new CountDownTimer(360000, 1000){
                    public void onTick(long millisUntilFinished){

                        sec = (millisUntilFinished / 1000) % 60;
                        min = (millisUntilFinished / (1000 * 60)) % 60;
                        hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;

                        timeRemaining = String.format("%02d:%02d:%02d", hour, min, sec);

                        textView.setText(timeRemaining);
                    }
                    public  void onFinish(){
                        textView.setText("Timer finished!");
                        //Send messages
                    }
                }.start();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numDrinks++;
                BAC = ((numDrinks*(alcOz*alcPercent)*5.14)/(weight*genConst)) - 0.15*(timeElapsed);

                if(BAC == 0) { status = "Sober"; dialNum = 1; }
                else if(BAC > 0 && BAC < 0.06){ status = "Buzzed"; dialNum = 2; }
                else if(BAC >= 0.06 && BAC < 0.1){ status = "Tipsy"; dialNum = 3; }
                else if(BAC >= 0.1 && BAC < 0.13){ status = "Slightly Drunk"; dialNum = 4; }
                else if(BAC >= 0.13 && BAC < 0.16){ status = "Moderately Drunk"; dialNum = 6; }
                else if(BAC >= 0.16 && BAC < 0.2){ status = "Extremely Drunk"; dialNum = 8; }
                else if(BAC >= 0.2 && BAC < 0.25){ status = "Wasted"; dialNum = 9; }
                else if(BAC >= 0.25 && BAC < 0.4){ status = "Alcohol Poisoning"; dialNum = 10; }
                else if(BAC >= 0.4) status = "R.I.P";
                else if(BAC >= 1.0) status = "Blood is Pure Alcohol";

                textViewBAC.setText(status);
            }
        });

    }
}
