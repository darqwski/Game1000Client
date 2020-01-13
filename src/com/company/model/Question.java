package com.company.model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Question {
    String text = null;
    ArrayList<String> answers = new ArrayList<>();

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    String correct = null;
    public Question(){}
    public Question(String text, String a1, String a2, String a3, String a4){
        setAnswers(new ArrayList<>());
        setText(text);
        addAnswer(a1);
        addAnswer(a2);
        addAnswer(a3);
        addAnswer(a4);
    }

    public boolean isCorrectAnswer(String answer){
        return  correct.equals(answer);
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public void addAnswer(String answer){
        this.answers.add(answer);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
