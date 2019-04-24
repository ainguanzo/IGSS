package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class AddDrink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_drink);
    }
    public void addNewDrink(View view){

        SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val",1);
        editor.commit();

        Intent intent = new Intent(AddDrink.this, AddAnotherDrink.class);
        startActivity(intent);

    }
    public void addCustomDrink(View view){
        Intent intent = new Intent(AddDrink.this, CustomDrink.class);
        startActivity(intent);
    }
    public void cancel(View view){

        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val",0);
        editor.commit();

        Intent openMainActivity= new Intent(AddDrink.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }
}
