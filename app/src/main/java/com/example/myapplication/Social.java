package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Social extends AppCompatActivity {
    private ImageButton button;
    private ListView listView;

    //hardcoded friend names
    String[] friends = {"Arya Stark", "Jon Snow", "Tyrion Lannister"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        //creating the list on the Social page
        listView= findViewById(R.id.listview);
        ArrayList<String> arrayList = new ArrayList<>();

        //adding friends to the list
        arrayList.add(friends[0]);
        arrayList.add(friends[1]);
        arrayList.add(friends[2]);

        //array adapter with code to change the list elements to be white text
        ArrayAdapter arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);

                return view;
            }
        };

        listView.setAdapter(arrayAdapter);


    }

    public void goHome(View view)
    {

        SharedPreferences sharedPref = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress_val",0);
        editor.commit();

        Intent openMainActivity= new Intent(Social.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }

    public void goToAddDrink(View view)
    {
        Intent intent = new Intent(Social.this, AddDrink.class);
        startActivity(intent);
    }
}
