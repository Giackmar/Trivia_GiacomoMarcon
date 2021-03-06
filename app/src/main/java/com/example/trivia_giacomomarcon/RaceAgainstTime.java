package com.example.trivia_giacomomarcon;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class RaceAgainstTime extends AppCompatActivity {

    View view_RAT;

    //parametri input da "impostazioni"
    String category;
    int questionsCounter;
    int timePerQuestion;

    ArrayList<String> categories;

    ArrayList<Question> questions;
    int currentQuestionNumber;
    ArrayAdapter<String> adapter;

    String url;

    CountDownTimer timer;
    int timeUntilFinished;

    TextView tv_RAT_timer;
    TextView tv_RAT_question;
    ListView lv_RAT_answers;

    TextView tv_RATST_title;
    TextView tv_RATST_meanResponseTime;
    TextView tv_RATST_answers;
    Button b_RATST_restart;
    Button b_RATST_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_against_time);

        view_RAT = findViewById(R.id.view_RAT);

        //carico i dati passati dall'altra activity
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        questionsCounter = intent.getIntExtra("questionsCounter",10);
        timePerQuestion = intent.getIntExtra("timePerQuestion",10);

        //carico le categorie nell'arrayList
        categories = new ArrayList<String>();
        loadCategory();

        //genero il mio url
        url = generateUrl();

        //collego grafica a codice
        tv_RAT_timer = findViewById(R.id.tv_RAT_timer);
        tv_RAT_question = findViewById(R.id.tv_RAT_question);
        lv_RAT_answers = findViewById(R.id.lv_RAT_answers);


        //variabili per gestione delle domande-risposte
        questions = new ArrayList<>();
        currentQuestionNumber = -1;
        timeUntilFinished = 0;

        //scarico i dati (le domande e risposte)
        try {
            loadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        lv_RAT_answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (winOrLose(questions.get(currentQuestionNumber).getAnswers().get(position))) {
                    anim(1);
                    endQuestion(1);
                }
                else
                    anim(2);
                    endQuestion(2);
            }
        });
    }


    String generateUrl() {
        String myUrl = "https://opentdb.com/api.php?amount="+questionsCounter+"&category="+ getCategoryCode();
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

    void anim(int code){
        int color = 0;
        if(code==1)
        {
            color = Color.GREEN;
        }else if(code==2){
            color = Color.RED;
        }else if(code==0){
            color = Color.BLUE;
        }

        ValueAnimator opening_anim = ValueAnimator.ofArgb(Color.BLACK, color);
        opening_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view_RAT.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator closing_anim = ValueAnimator.ofArgb(color, Color.BLACK);
        closing_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view_RAT.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
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


    void startQuestion() {
        loadQuestion();
        timer = new CountDownTimer((timePerQuestion+1)*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_RAT_timer.setText("Remaining time\n             " + millisUntilFinished / 1000);
                timeUntilFinished = (int)millisUntilFinished/1000;
            }

            public void onFinish() {
                tv_RAT_timer.setText("done!");
                anim(0);
                endQuestion(0);
            }

        }.start();
    }

    void endQuestion(int code) {
        questions.get(currentQuestionNumber).setCode(code);
        questions.get(currentQuestionNumber).setTime(timePerQuestion-timeUntilFinished);
        timer.cancel();
        if(currentQuestionNumber==questionsCounter-1)
        {
            loadStatsLayout(code);
            return;
        }
        startQuestion();
    }


    void loadStatsLayout(int code) {
        setContentView(R.layout.activity_race_against_time_stats);

        //------------------------------------------------------------------------------------
        int color = 0;
        if(code==1)
        {
            color = Color.GREEN;
        }else if(code==2){
            color = Color.RED;
        }else if(code==0){
            color = Color.BLUE;
        }
        View view_RATS = findViewById(R.id.view_RATS);
        ValueAnimator opening_anim = ValueAnimator.ofArgb(Color.BLACK, color);
        opening_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view_RATS.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator closing_anim = ValueAnimator.ofArgb(color, Color.BLACK);
        closing_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view_RATS.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
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
        //------------------------------------------------------------------------------------

        tv_RATST_title = findViewById(R.id.tv_RATST_title);
        tv_RATST_meanResponseTime = findViewById(R.id.tv_RATST_meanResponseTime);
        tv_RATST_answers = findViewById(R.id.tv_RATST_answers);
        b_RATST_restart = findViewById(R.id.b_RATST_restart);
        b_RATST_home = findViewById(R.id.b_RATST_home);
        tv_RATST_title.setText("Game stats");
        tv_RATST_meanResponseTime.setText(meanResponseTime());
        tv_RATST_answers.setText(answerStats());

        b_RATST_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent restart = new Intent(RaceAgainstTime.this, RaceAgainstTimeSetting.class);
                restart.putExtra("category", category);
                restart.putExtra("questionsCounter", questionsCounter);
                restart.putExtra("timePerQuestion", timePerQuestion);
                startActivity(restart);
                finish();
            }
        });
        b_RATST_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(RaceAgainstTime.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });
    }

    String meanResponseTime(){
        int timeSum = 0;
        boolean trueData = false;
        for (Question question:questions
             ) {
            if(question.getCode()!=0) {
                timeSum += question.getTime();
                trueData = true;
            }
        }
        int meanTime = timeSum/questionsCounter;
        if(!trueData){
            return "Mean response time: insufficient data";
        }
        if(meanTime==0){
            return "Mean response time: <1 sec";
        }
        return "Mean response time: "+meanTime+" sec";
    }

    String answerStats(){
        int correctAnswersCount = 0;
        int incorrectAnswersCount = 0;
        int notGivenAnswersCount = 0;
        for (Question question:questions
             ) {
            if(question.getCode()==0)
            {
                notGivenAnswersCount++;
            }
            else if(question.getCode()==1)
            {
                correctAnswersCount++;
            }
            else if(question.getCode()==2)
            {
                incorrectAnswersCount++;
            }
        }
        String result = "Correct answers: "+correctAnswersCount+"\nIncorrect answers: "+incorrectAnswersCount+"\nNot given answers: "+notGivenAnswersCount;
        return result;
    }


    public void loadData() throws MalformedURLException {
        DownloadInternet downloadInternet = new DownloadInternet();
        URL myUrl = new URL(url);
        downloadInternet.execute(myUrl);
    }

    class DownloadInternet extends AsyncTask<URL, String, String> {

            final ProgressDialog dialog = new ProgressDialog(RaceAgainstTime.this);
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

            @Override
            protected void onPostExecute(String strings) {

                dialog.dismiss();
                if(strings.length()<50)
                {
                    Toast.makeText(getApplicationContext(), "Non sono presenti abbastanza domande con i parametri richiesti\nProva cambiando alcuni parametri", Toast.LENGTH_LONG).show();
                    Intent survivalSetting = new Intent(RaceAgainstTime.this, RaceAgainstTimeSetting.class);
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
        tv_RAT_question.setText(currentQuestion.getQuestion());
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
        lv_RAT_answers.setAdapter(adapter);
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
        Intent restart = new Intent(RaceAgainstTime.this, RaceAgainstTimeSetting.class);
        restart.putExtra("category", category);
        restart.putExtra("questionsCounter", questionsCounter);
        restart.putExtra("timePerQuestion", timePerQuestion);
        startActivity(restart);
        finish();
    }
}
