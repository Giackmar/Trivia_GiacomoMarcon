package com.example.trivia_giacomomarcon;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

    View view_S;

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

    int life = 3;
    int score = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival);

        view_S = findViewById(R.id.view_S);

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
                    Anim(1);
                    endQuestion(1);
                }
                else
                {
                    Anim(2);
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

    void Anim(int code){
        int color = 0;
        if(code==1)
        {
            color = Color.GREEN;
        }else if(code==2){
            color = Color.RED;
        }

        ValueAnimator opening_anim = ValueAnimator.ofArgb(Color.BLACK, color);
        opening_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view_S.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator closing_anim = ValueAnimator.ofArgb(color, Color.BLACK);
        closing_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view_S.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        opening_anim.setDuration(100);
        closing_anim.setDuration(300);

        opening_anim.start();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                closing_anim.start();
            }
        }, 200);
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
        tv_S_lifeCounter.setText("Life "+life);
        tv_S_score.setText("Score "+score);
        start = Instant.now() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void endQuestion(int code) {
        if(code==2) {
            life--;
            tv_S_lifeCounter.setText("Life "+life);
            if(life==0) {
                loadStatsLayout();
                return;
            }
        }
        else
        {
            score++;
            tv_S_score.setText("Score "+score);
        }
        questions.get(currentQuestionNumber).setCode(code);
        end = Instant.now();
        Duration elapsedTime_duration = Duration.between(start,end);
        String elapsedTime_string = elapsedTime_duration.toString();
        Double minutes = 0.0;
        if(elapsedTime_string.contains("PT"))
        {
            elapsedTime_string = elapsedTime_string.replace("PT","");
        }
        if(elapsedTime_string.contains("S"))
        {
            elapsedTime_string = elapsedTime_string.replace("S","");
        }
        if(elapsedTime_string.contains("M"))
        {
            minutes =  Double.parseDouble(elapsedTime_string.substring(0, elapsedTime_string.indexOf("M")));
            minutes = minutes*60;
            elapsedTime_string = elapsedTime_string.substring(elapsedTime_string.indexOf("M")+1, elapsedTime_string.length());
        }
        double elapsedTime = 0;
        try{
            elapsedTime = Double.parseDouble(elapsedTime_string);
        }catch (Exception e)
        {
            elapsedTime = 60.0;
        }
        elapsedTime = Double.sum(elapsedTime,minutes);
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
                Intent restart = new Intent(Survival.this, SurvivalSetting.class);
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
        meanTime = Math.floor(meanTime * 100.0) / 100.0;
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
        String result = "Total score: "+score;
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
                Toast.makeText(getApplicationContext(), "Non sono presenti abbastanza domande con i parametri richiesti\nProva cambiando alcuni parametri", Toast.LENGTH_LONG).show();
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
            for(int a=0;a<incorrect_answers_json.length();a++)
            {
                incorrect_answers.add(incorrect_answers_json.get(a).toString());
            }
            for(int a=0; a<incorrect_answers.size();a++)
            {
                String newString = htmlDecoder(incorrect_answers.get(0));
                incorrect_answers.remove(0);
                incorrect_answers.add(newString);
            }
            category = htmlDecoder(category);
            type = htmlDecoder(type);
            difficulty = htmlDecoder(difficulty);
            question = htmlDecoder(question);
            correct_answer = htmlDecoder(correct_answer);
            ArrayList<String> a = incorrect_answers;
            Question myQuestion = new Question(category,type,difficulty,question,correct_answer,incorrect_answers);
            questions.add(myQuestion);
        }
    }

    public String htmlDecoder(String myString)
    {
        return unescapeHtml4(myString).replace("&amp;","&").replace("&gt;",">").replace("&lt;","<").replace("&quot;","\"");
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