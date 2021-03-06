package com.example.trivia_giacomomarcon;

import java.util.ArrayList;
import java.util.Collections;

public class Question {

    String category;
    String type;
    String difficulty;
    String question;
    String correct_answer;
    ArrayList<String> answers;

    //question stats
    int code;
    int time;
    double elapseTime;

    public Question(String category, String type, String difficulty, String question, String correct_answer, ArrayList<String> answers)
    {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.answers = answers;
        this.answers.add(correct_answer);
        Collections.shuffle(answers);
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    //question stats


    public void setCode(int code) {
        this.code = code;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setElapseTime(double elapseTime) {
        this.elapseTime = elapseTime;
    }

    public int getCode() {
        return code;
    }

    public int getTime() {
        return time;
    }

    public double getElapseTime() {
        return elapseTime;
    }
}
