package com.gistMED.gistmd.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class InformationPackage {

    private String label; //the question
    private String ID; //the question id (question_1) for example
    private String subLabel; //sub label for question page
    private String viewID;
    private String collectionView;

    private ArrayList<Answer> answers = new ArrayList<>(); //possible answers for the question

    public InformationPackage(String label,String subLabel,String ID,String viewID) {
        this.label = label;
        this.subLabel = subLabel;
        this.ID = ID;
        this.viewID = viewID;
    }

    public String getCollectionView() {
        return this.collectionView;
    }

    public void setCollectionView(String collectionView) {
        this.collectionView = collectionView;
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

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }

    public String getViewID() {
        return viewID;
    }

    public void setViewID(String viewID) {
        this.viewID = viewID;
    }

    public void setAnswerSelected(Answer answerSelected)  //set new answer as selected (because only one can be selected)
    {
        for (Answer answer:
                this.answers) {
            if(answer.getID().equals(answerSelected.getID()))
                answer.setSelected(true);
            else
            {
                answer.setSelected(false);
            }
        }
    }

    public boolean checkIcons() //check if all icons got their url from database
    {
        boolean flag = true;
        for (Answer answer:
             this.answers) {
            if(answer.getURL()==null)
                flag = false;
        }
        return flag;
    }

    public String getAnswerIdByLabel(String label) //get an answer id by her label
    {
        for (Answer answer:
             this.answers) {
            if(answer.getLabel().equals(label))
                return answer.getID();
        }
        return "";
    }
}