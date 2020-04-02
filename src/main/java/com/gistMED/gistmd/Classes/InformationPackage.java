package com.gistMED.gistmd.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class InformationPackage {

    private String label;
    private String ID;

    private ArrayList<Answer> answers = new ArrayList<>();

    public InformationPackage(String label,String ID) {
        this.label = label;
        this.ID = ID;
    }

    public void AddAnswer(Answer answerToAdd) {
        this.answers.add(answerToAdd);
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public String getID() {
        return ID;
    }
}