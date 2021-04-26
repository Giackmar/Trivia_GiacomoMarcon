package com.example.trivia_giacomomarcon;

import java.util.ArrayList;

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

    public Question(String category, String type, String difficulty, String question, String correct_answer, ArrayList<String> answers)
    {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.answers = answers;
        this.answers.add(correct_answer);
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

    public int getCode() {
        return code;
    }

    public int getTime() {
        return time;
    }
}
