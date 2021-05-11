package com.example.trivia_giacomomarcon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RaceAgainstTimeSetting extends AppCompatActivity {

    Button btn_RATS_start;

    int questionsCounter;
    int timePerQuestion;
    String selectedCategory;
    ArrayList<String> anyCategory;


    NumberPicker np_RATS_categories;
    NumberPicker np_RATS_questionsCounter;
    NumberPicker np_RATS_timePerQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_against_time_setting);

        btn_RATS_start = findViewById(R.id.btn_RATS_start);

        //valori di default
        questionsCounter = 1;
        timePerQuestion = 1;
        selectedCategory = "Any Category";

        anyCategory = new ArrayList<>();



        loadCategory();

        ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,anyCategory);

        btn_RATS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent raceAgainstTime = new Intent(RaceAgainstTimeSetting.this, RaceAgainstTime.class);

                    raceAgainstTime.putExtra("category", selectedCategory);
                    raceAgainstTime.putExtra("questionsCounter", questionsCounter);
                    raceAgainstTime.putExtra("timePerQuestion", timePerQuestion);
                    startActivity(raceAgainstTime);
                    finish();
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

        np_RATS_questionsCounter= (NumberPicker) findViewById(R.id.np_RATS_questionsCounter);
        int questionsCounterMaxValue = 50;
        int[] arrayInt_questionsCounter = new int[questionsCounterMaxValue];
        String[] arrayString_questionsCounter = new String[questionsCounterMaxValue];
        for(int i=0; i<questionsCounterMaxValue; i++)
        {
            arrayInt_questionsCounter[i] = i+1;
            arrayString_questionsCounter[i] = Integer.toString(i+1);
        }
        np_RATS_questionsCounter.setMinValue(0);
        np_RATS_questionsCounter.setMaxValue(questionsCounterMaxValue - 1);
        np_RATS_questionsCounter.setDisplayedValues(arrayString_questionsCounter);
        np_RATS_questionsCounter.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_RATS_questionsCounter.setWrapSelectorWheel(false);
        NumberPicker.OnValueChangeListener myValChangedListener_questionsCounter = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                questionsCounter = arrayInt_questionsCounter[newVal];
            }
        };
        np_RATS_questionsCounter.setOnValueChangedListener(myValChangedListener_questionsCounter);

        np_RATS_timePerQuestion= (NumberPicker) findViewById(R.id.np_RATS_timePerQuestion);
        int timePerQuestionMaxValue = 30;
        int[] arrayInt_timePerQuestion = new int[timePerQuestionMaxValue];
        String[] arrayString_timePerQuestion = new String[timePerQuestionMaxValue];
        for(int i=0; i<timePerQuestionMaxValue; i++)
        {
            arrayInt_timePerQuestion[i] = i+1;
            arrayString_timePerQuestion[i] = Integer.toString(i+1);
        }
        np_RATS_timePerQuestion.setMinValue(0);
        np_RATS_timePerQuestion.setMaxValue(timePerQuestionMaxValue - 1);
        np_RATS_timePerQuestion.setDisplayedValues(arrayString_timePerQuestion);
        np_RATS_timePerQuestion.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_RATS_timePerQuestion.setWrapSelectorWheel(false);
        NumberPicker.OnValueChangeListener myValChangedListener_timePerQuestion = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                timePerQuestion = arrayInt_timePerQuestion[newVal];
            }
        };
        np_RATS_timePerQuestion.setOnValueChangedListener(myValChangedListener_timePerQuestion);
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