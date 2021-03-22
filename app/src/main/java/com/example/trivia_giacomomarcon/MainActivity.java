package com.example.trivia_giacomomarcon;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


public class MainActivity extends AppCompatActivity {

    String url;

    ArrayAdapter adapter;

    ArrayList<Question> questions;
    int currentQuestionNumber;
    ArrayList<String> answers;

    ListView listView_answers;
    TextView textView_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //answers = new ArrayList<String>();
        //correctAnswer = new String();

        url = "https://opentdb.com/api.php?amount=10";

        questions = new ArrayList<>();
        currentQuestionNumber = 0;
        answers = new ArrayList<>();

        listView_answers = findViewById(R.id.listView_answers);
        textView_question = findViewById(R.id.textView_question);

        try {
            loadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        listView_answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(winOrLose(questions.get(currentQuestionNumber).getAnswers().get(position)))
                {
                    currentQuestionNumber++;
                    loadQuestion();
                }
            }
        });
    }

    public void loadData() throws MalformedURLException {
        DownloadInternet downloadInternet = new DownloadInternet();
        URL myUrl = new URL(url);
        downloadInternet.execute(myUrl);
    }

    private class DownloadInternet extends AsyncTask<URL, String, String> {

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
            try {
                parsingJson(strings);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            loadQuestion();
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

    public void loadQuestion()
    {
        Question currentQuestion = questions.get(currentQuestionNumber);
        textView_question.setText(currentQuestion.getQuestion());
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
        listView_answers.setAdapter(adapter);
    }

    public boolean winOrLose(String answer)
    {
        Question currentQuestion = questions.get(currentQuestionNumber);
        if(answer==currentQuestion.getCorrect_answer())
        {
            return true;
        }
        return false;
    }
}