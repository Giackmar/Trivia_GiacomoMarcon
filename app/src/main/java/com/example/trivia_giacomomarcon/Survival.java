package com.example.trivia_giacomomarcon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class Survival extends AppCompatActivity {

    //parametri input da "impostazioni"
    String category;
    String difficulty;
    String type;

    ArrayList<String> categories;

    ArrayList<Question> questions;
    int currentQuestionNumber;
    ArrayAdapter<String> adapter;

    String url;


    TextView tv_S_lifeCounter;
    TextView tv_S_score;
    TextView tv_S_question;
    ListView lv_S_answers;

    //STATS
    TextView tv_SST_title;
    TextView tv_SST_meanResponseTime;
    TextView tv_SST_answers;
    Button b_SST_restart;
    Button b_SST_home;
    int questionsCounter = 10;

    Instant start;
    Instant end;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival);

        //carico i dati passati dall'altra activity
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        type = intent.getStringExtra("type");
        difficulty = intent.getStringExtra("difficulty");

        //carico le categorie nell'arrayList
        categories = new ArrayList<String>();
        loadCategory();

        //genero il mio url
        url = generateUrl(questionsCounter);

        //collego grafica a codice
        tv_S_lifeCounter = findViewById(R.id.tv_S_lifeCounter);
        tv_S_score = findViewById(R.id.tv_S_score);
        tv_S_question = findViewById(R.id.tv_S_question);
        lv_S_answers = findViewById(R.id.lv_S_answers);

        //variabili per gestione delle domande-risposte
        questions = new ArrayList<>();
        currentQuestionNumber = -1;

        try {
            loadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        lv_S_answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (winOrLose(questions.get(currentQuestionNumber).getAnswers().get(position))) {
                    endQuestion(1);
                }
                else
                {
                    endQuestion(2);
                }
            }
        });
    }

    String generateUrl(int size) {
        String myUrl = "https://opentdb.com/api.php?amount="+size;
        if(!category.equals("Any Category"))
        {
            myUrl+="&category="+ getCategoryCode();
        }
        if(!difficulty.equals("Any Difficulty"))
        {
            myUrl+="&difficulty="+difficulty.toLowerCase();
        }
        if(!type.equals("Any Type"))
        {
            if(type.equals("Multiple Choice"))
            myUrl+="&type=multiple";

            if(type.equals("True/False"))
            myUrl+="&type=boolean";
        }
        return myUrl;
    }

    int getCategoryCode() {
        for(int i=0; i<categories.size();i++ )
        {
            if(categories.get(i).compareTo(category)==0)
                return i+9;
        }
        return 0;
    }

    void loadCategory() {
        categories.add("General knowledge");
        categories.add("Books");
        categories.add("Film");
        categories.add("Music");
        categories.add("Musicals & Theatres");
        categories.add("Television");
        categories.add("Video Games");
        categories.add("Board Games");
        categories.add("Science & Nature");
        categories.add("Computers");
        categories.add("Mathematics");
        categories.add("Mythology");
        categories.add("Sports");
        categories.add("Geography");
        categories.add("History");
        categories.add("Politics");
        categories.add("Celebrities");
        categories.add("Art");
        categories.add("Animals");
        categories.add("Vehicles");
        categories.add("Comics");
        categories.add("Gadgets");
        categories.add("Japanese Anime & Manga");
        categories.add("Cartoon & Animations");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void startQuestion() {
        loadQuestion();
        start = Instant.now() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void endQuestion(int code) {
        questions.get(currentQuestionNumber).setCode(code);
        end = Instant.now();
        Duration elapsedTime_duration = Duration.between(start,end);
        String elapsedTime_string = elapsedTime_duration.toString();
        elapsedTime_string = elapsedTime_string.replace("PT","").replace("S","");
        double elapsedTime = Double.parseDouble(elapsedTime_string);
        questions.get(currentQuestionNumber).setElapseTime(elapsedTime);
        if(currentQuestionNumber==questionsCounter-1)
        {
            loadStatsLayout();
            return;
        }
        startQuestion();
    }


    void loadStatsLayout() {
        setContentView(R.layout.activity_survival_stats);

        tv_SST_title = findViewById(R.id.tv_SST_title);
        tv_SST_meanResponseTime = findViewById(R.id.tv_SST_meanResponseTime);
        tv_SST_answers = findViewById(R.id.tv_SST_answers);
        b_SST_restart = findViewById(R.id.b_SST_restart);
        b_SST_home = findViewById(R.id.b_SST_home);
        tv_SST_title.setText("Game stats");
        tv_SST_meanResponseTime.setText(meanResponseTime());
        tv_SST_answers.setText(answerStats());

        b_SST_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent restart = new Intent(Survival.this, RaceAgainstTimeSetting.class);
                startActivity(restart);
                finish();
            }
        });
        b_SST_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Survival.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });
    }

    String meanResponseTime(){
        double timeSum = 0;
        for (Question question:questions
        ) {
            timeSum += question.getElapseTime();
        }
        double meanTime = timeSum/questionsCounter;
        return "Mean response time: "+meanTime+" sec";
    }

    String answerStats(){
        int correctAnswersCount = 0;
        int incorrectAnswersCount = 0;
        for (Question question:questions
        ) {
            if(question.getCode()==1)
            {
                correctAnswersCount++;
            }
            else if(question.getCode()==2)
            {
                incorrectAnswersCount++;
            }
        }
        int score = correctAnswersCount+incorrectAnswersCount;
        String result = "Total score: "+score+"\nCorrect answers: "+correctAnswersCount+"\nIncorrect answers: "+incorrectAnswersCount;
        return result;
    }


    public void loadData() throws MalformedURLException {
        Survival.DownloadInternet downloadInternet = new Survival.DownloadInternet();
        URL myUrl = new URL(url);
        downloadInternet.execute(myUrl);
    }

    private class DownloadInternet extends AsyncTask<URL, String, String> {

        final ProgressDialog dialog = new ProgressDialog(Survival.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading data...");
            dialog.show();
        }

        @Override
        protected String doInBackground(URL... strings) {

            String json = "";

            try {
                HttpURLConnection conn = (HttpURLConnection) strings[0].openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    json += line;
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String strings) {

            dialog.dismiss();
            if(strings.length()<50)
            {
                Toast.makeText(getApplicationContext(), "Non sono presenti abbastanza domande con i parametri richiesti.\nProva con altri parametri", Toast.LENGTH_LONG).show();
                Intent survivalSetting = new Intent(Survival.this, SurvivalSetting.class);
                startActivity(survivalSetting);
                finish();
                return;
            }
            try {
                parsingJson(strings);
                startQuestion();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void parsingJson(String json) throws JSONException {
        JSONObject jsonData = new JSONObject(json);
        JSONArray questionsJson =  jsonData.getJSONArray("results");
        for(int i=0; i<questionsJson.length();i++)
        {
            JSONObject questionJson = questionsJson.getJSONObject(i);
            String category = questionJson.getString("category");
            String type = questionJson.getString("type");
            String difficulty = questionJson.getString("difficulty");
            String question = questionJson.getString("question");
            String correct_answer = questionJson.getString("correct_answer");
            JSONArray incorrect_answers_json = questionJson.getJSONArray("incorrect_answers");
            ArrayList<String> incorrect_answers = new ArrayList<>();
            for (String s:incorrect_answers
            ) {
                s = unescapeHtml4(s);
            }
            category = unescapeHtml4(category);
            type = unescapeHtml4(type);
            difficulty = unescapeHtml4(difficulty);
            question = unescapeHtml4(question);
            correct_answer = unescapeHtml4(correct_answer);
            for(int a=0; a<incorrect_answers_json.length();a++)
            {
                incorrect_answers.add(incorrect_answers_json.getString(a));
            }
            Question myQuestion = new Question(category,type,difficulty,question,correct_answer,incorrect_answers);
            questions.add(myQuestion);
        }
    }

    public void loadQuestion() {
        currentQuestionNumber++;
        Question currentQuestion = questions.get(currentQuestionNumber);
        tv_S_question.setText(currentQuestion.getQuestion());
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row, R.id.textView_answer, currentQuestion.getAnswers()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView_answer = view.findViewById(R.id.textView_answer);
                textView_answer.setText(questions.get(currentQuestionNumber).getAnswers().get(position));
                return view;
            }
        };
        lv_S_answers.setAdapter(adapter);
    }

    public boolean winOrLose(String answer) {
        Question currentQuestion = questions.get(currentQuestionNumber);
        if(answer==currentQuestion.getCorrect_answer())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent survivalSetting = new Intent(Survival.this, SurvivalSetting.class);
        startActivity(survivalSetting);
        finish();
    }
}