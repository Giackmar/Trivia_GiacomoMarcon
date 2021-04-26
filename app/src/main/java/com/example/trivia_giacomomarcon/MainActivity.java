package com.example.trivia_giacomomarcon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button btn_RaceAgainstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_RaceAgainstTime = findViewById(R.id.btn_RaceAgainstTime);



        btn_RaceAgainstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent raceAgainstTimeSetting = new Intent(MainActivity.this, RaceAgainstTimeSetting.class);
                startActivity(raceAgainstTimeSetting);
                finish();
            }
        });
    }


}