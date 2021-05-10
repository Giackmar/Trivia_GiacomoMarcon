package com.example.trivia_giacomomarcon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RaceAgainstTimeSetting extends AppCompatActivity {

    EditText etn_RATS_questionsCounter;
    EditText etn_RATS_timePerQuestion;
    ListView lv_RATS_category;
    TextView tv_RATS_selectedCategory;
    Button btn_RATS_start;

    int questionsCounter;
    int timePerQuestion;
    String selectedCategory;
    ArrayList<String> anyCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_against_time_setting);

        etn_RATS_questionsCounter = findViewById(R.id.etn_RATS_questionsCounter);
        etn_RATS_timePerQuestion = findViewById(R.id.etn_RATS_timePerQuestion);
        lv_RATS_category = findViewById(R.id.lv_RATS_topics);
        tv_RATS_selectedCategory = findViewById(R.id.tv_RATS_selectedCategory);
        btn_RATS_start = findViewById(R.id.btn_RATS_start);

        questionsCounter = 0;
        timePerQuestion = 0;
        anyCategory = new ArrayList<>();

        loadCategory();

        ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,anyCategory);

        lv_RATS_category.setAdapter(myAdapter);

        lv_RATS_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_RATS_selectedCategory.setText(anyCategory.get(position));
            }
            });

        lv_RATS_category.setOnScrollListener(new AbsListView.OnScrollListener(){
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                tv_RATS_selectedCategory.setText(anyCategory.get(lv_RATS_category.getFirstVisiblePosition()));
            }
        });


        btn_RATS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSetting();
                if(questionsCounter>0 && timePerQuestion>0)
                {
                    Intent raceAgainstTime = new Intent(RaceAgainstTimeSetting.this, RaceAgainstTime.class);

                    raceAgainstTime.putExtra("category", tv_RATS_selectedCategory.getText().toString());
                    raceAgainstTime.putExtra("questionsCounter", questionsCounter);
                    raceAgainstTime.putExtra("timePerQuestion", timePerQuestion);
                    startActivity(raceAgainstTime);
                }
            }
        });


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
}