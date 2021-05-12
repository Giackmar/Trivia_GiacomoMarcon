package com.example.trivia_giacomomarcon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SurvivalSetting extends AppCompatActivity {

    Button btn_SS_start;


    String selectedCategory;
    String selectedType;
    String selectedDifficulty;
    ArrayList<String> anyCategory;


    NumberPicker np_SS_categories;
    NumberPicker np_SS_type;
    NumberPicker np_SS_difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival_setting);

        btn_SS_start = findViewById(R.id.btn_SS_start);

        //valori di default
        selectedType = "Any Type";
        selectedDifficulty = "Any Difficulty";
        selectedCategory = "Any Category";

        anyCategory = new ArrayList<>();



        loadCategory();


        btn_SS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent survival = new Intent(SurvivalSetting.this, Survival.class);

                survival.putExtra("category", selectedCategory);
                survival.putExtra("difficulty", selectedDifficulty);
                survival.putExtra("type", selectedType);
                startActivity(survival);
                finish();
            }
        });


        np_SS_categories= (NumberPicker) findViewById(R.id.np_SS_categories);
        String[] stringArray = new String[anyCategory.size()];
        final String myArray[] = anyCategory.toArray(stringArray);
        np_SS_categories.setMinValue(0);
        np_SS_categories.setMaxValue(myArray.length - 1);
        np_SS_categories.setDisplayedValues(myArray);
        np_SS_categories.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedCategory = stringArray[newVal];
            }
        };
        np_SS_categories.setOnValueChangedListener(myValChangedListener);

        np_SS_type= (NumberPicker) findViewById(R.id.np_SS_type);
        int typesCounter = 3;
        String[] array_types = {"Any Type","Multiple Choice","True/False"};
        np_SS_type.setMinValue(0);
        np_SS_type.setMaxValue(typesCounter - 1);
        np_SS_type.setDisplayedValues(array_types);
        np_SS_type.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_SS_type.setWrapSelectorWheel(false);
        NumberPicker.OnValueChangeListener myValChangedListener_type = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedType = array_types[newVal];
            }
        };
        np_SS_type.setOnValueChangedListener(myValChangedListener_type);

        np_SS_difficulty= (NumberPicker) findViewById(R.id.np_SS_difficulty);
        int difficultyCounter = 4;
        String[] array_difficulty = {"Any Difficulty","Easy","Medium","Hard"};
        np_SS_difficulty.setMinValue(0);
        np_SS_difficulty.setMaxValue(difficultyCounter - 1);
        np_SS_difficulty.setDisplayedValues(array_difficulty);
        np_SS_difficulty.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_SS_difficulty.setWrapSelectorWheel(false);
        NumberPicker.OnValueChangeListener myValChangedListener_difficulty = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedDifficulty = array_difficulty[newVal];
            }
        };
        np_SS_difficulty.setOnValueChangedListener(myValChangedListener_difficulty);
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
        Intent mainActivity = new Intent(SurvivalSetting.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}