package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CustomDrink extends AppCompatActivity {

    private static SeekBar slider;
    private static SeekBar sliderPercent;
    private static ImageView liquid_level;


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void seekBarInit(){
        slider = (SeekBar)findViewById(R.id.seekBar1);
        sliderPercent = (SeekBar)findViewById(R.id.seekBar2);
        liquid_level = (ImageView)findViewById(R.id.liquid);
        ViewGroup.LayoutParams params = liquid_level.getLayoutParams();
        params.height = slider.getProgress()*3;
        liquid_level.setLayoutParams(params);

        SharedPreferences preferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("drink_percent", 100);
        editor.putInt("drink_volume", 50);
        editor.commit();


        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                ViewGroup.LayoutParams params = liquid_level.getLayoutParams();
                params.height = progress*4;
                liquid_level.setLayoutParams(params);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ViewGroup.LayoutParams params = liquid_level.getLayoutParams();
                params.height = progress_value*4;
                liquid_level.setLayoutParams(params);

                SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("progress_val",1);
                editor.putInt("drink_volume", progress_value);
                editor.commit();


            }
        });

        sliderPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("drink_percent", progress_value);
                editor.commit();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_drink);
        seekBarInit();
        slider.setProgress(50);
    }

    public void goHome(View view){
        Intent openMainActivity= new Intent(CustomDrink.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }
    public void cancel(View view){

        SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val",0);
        editor.commit();

        Intent openMainActivity= new Intent(CustomDrink.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }


}

