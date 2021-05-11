package com.example.trivia_giacomomarcon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;


public class MainActivity extends AppCompatActivity {

    Button btn_RaceAgainstTime;
    Button btn_ChooseTheRightAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_YES);


        btn_RaceAgainstTime = findViewById(R.id.btn_RaceAgainstTime);
        btn_ChooseTheRightAnswer = findViewById(R.id.btn_ChooseTheRightAnswer);


        btn_RaceAgainstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent raceAgainstTimeSetting = new Intent(MainActivity.this, RaceAgainstTimeSetting.class);
                startActivity(raceAgainstTimeSetting);
                finish();
            }
        });

        btn_ChooseTheRightAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "This feature is still on development", Toast.LENGTH_SHORT).show();
            }
        });
    }
}