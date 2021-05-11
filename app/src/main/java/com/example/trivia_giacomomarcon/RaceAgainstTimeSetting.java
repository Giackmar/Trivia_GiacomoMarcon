package com.example.trivia_giacomomarcon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RaceAgainstTimeSetting extends AppCompatActivity {

    EditText etn_RATS_questionsCounter;
    EditText etn_RATS_timePerQuestion;
    Button btn_RATS_start;

    int questionsCounter;
    int timePerQuestion;
    String selectedCategory;
    ArrayList<String> anyCategory;


    NumberPicker np_RATS_categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_against_time_setting);

        etn_RATS_questionsCounter = findViewById(R.id.etn_RATS_questionsCounter);
        etn_RATS_timePerQuestion = findViewById(R.id.etn_RATS_timePerQuestion);
        btn_RATS_start = findViewById(R.id.btn_RATS_start);

        questionsCounter = 0;
        timePerQuestion = 0;
        anyCategory = new ArrayList<>();

        selectedCategory = "Any Category";

        loadCategory();

        ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,anyCategory);

        btn_RATS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSetting();
                if(questionsCounter>0 && timePerQuestion>0)
                {
                    Intent raceAgainstTime = new Intent(RaceAgainstTimeSetting.this, RaceAgainstTime.class);

                    raceAgainstTime.putExtra("category", selectedCategory);
                    raceAgainstTime.putExtra("questionsCounter", questionsCounter);
                    raceAgainstTime.putExtra("timePerQuestion", timePerQuestion);
                    startActivity(raceAgainstTime);
                    finish();
                }
            }
        });


        np_RATS_categories= (NumberPicker) findViewById(R.id.np_RATS_categories);
        String[] stringArray = new String[anyCategory.size()];
        final String myArray[] = anyCategory.toArray(stringArray);
        np_RATS_categories.setMinValue(0);
        np_RATS_categories.setMaxValue(myArray.length - 1);
        np_RATS_categories.setDisplayedValues(myArray);
        np_RATS_categories.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
               selectedCategory = stringArray[newVal];
            }
        };
        np_RATS_categories.setOnValueChangedListener(myValChangedListener);

    }

    void loadSetting()
    {
        if(!TextUtils.isEmpty(etn_RATS_questionsCounter.getText().toString()))
        {
            questionsCounter = Integer.parseInt(etn_RATS_questionsCounter.getText().toString());
        }
        else
        {
            questionsCounter = 0;
        }
        if(!TextUtils.isEmpty(etn_RATS_timePerQuestion.getText().toString()))
        {
            timePerQuestion = Integer.parseInt(etn_RATS_timePerQuestion.getText().toString());
        }
        else
        {
            timePerQuestion = 0;
        }
    }

    void loadCategory()
    {
        anyCategory.add("Any Category");
        anyCategory.add("General knowledge");
        anyCategory.add("Books");
        anyCategory.add("Film");
        anyCategory.add("Music");
        anyCategory.add("Musicals & Theatres");
        anyCategory.add("Television");
        anyCategory.add("Video Games");
        anyCategory.add("Board Games");
        anyCategory.add("Science & Nature");
        anyCategory.add("Computers");
        anyCategory.add("Mathematics");
        anyCategory.add("Mythology");
        anyCategory.add("Sports");
        anyCategory.add("Geography");
        anyCategory.add("History");
        anyCategory.add("Politics");
        anyCategory.add("Celebrities");
        anyCategory.add("Art");
        anyCategory.add("Animals");
        anyCategory.add("Vehicles");
        anyCategory.add("Comics");
        anyCategory.add("Gadgets");
        anyCategory.add("Japanese Anime & Manga");
        anyCategory.add("Cartoon & Animations");
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(RaceAgainstTimeSetting.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}