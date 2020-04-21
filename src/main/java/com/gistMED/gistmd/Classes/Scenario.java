package com.gistMED.gistmd.Classes;

import android.service.voice.VoiceInteractionService;

import java.util.ArrayList;

public class Scenario {
    private String scenarioID;
    private String titleID;
    private String sub_tileID_1;
    private String sub_titleID_2;
    private String flowID;
    private String question_set_id;
    private ArrayList<CaseQuestion> caseQuestions =  new ArrayList<>();
    private Boolean FINAL_SCENARIO = false;
    private Boolean isCellSelected =false;
    private String iconID;
    private String iconURL;

    public Scenario(String scenarioID, String titleID, String sub_tileID_1, String sub_titleID_2, String flowID, String question_set_id,String iconID) {
        this.scenarioID = scenarioID;
        this.titleID = titleID;
        this.sub_tileID_1 = sub_tileID_1;
        this.sub_titleID_2 = sub_titleID_2;
        this.flowID = flowID;
        this.question_set_id = question_set_id;
        this.iconID = iconID;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public Boolean getCellSelected() {
        return isCellSelected;
    }

    public String getIconID() {
        return iconID;
    }

    public void setIconID(String iconID) {
        this.iconID = iconID;
    }

    public void ChangeState() {
        if(this.isCellSelected)
            this.isCellSelected = false;
        else
            this.isCellSelected = true;
    }

    public Boolean getFINAL_SCENARIO() {
        return FINAL_SCENARIO;
    }

    public void setFINAL_SCENARIO(Boolean FINAL_SCENARIO) {
        this.FINAL_SCENARIO = FINAL_SCENARIO;
    }

    public String getScenarioID() {
        return scenarioID;
    }

    public ArrayList<CaseQuestion> getCaseQuestions() {
        return caseQuestions;
    }

    public void setCaseQuestions(CaseQuestion caseQuestions) {
        this.caseQuestions.add(caseQuestions);
    }

    public void setScenarioID(String scenarioID) {
        this.scenarioID = scenarioID;
    }

    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public String getSub_tileID_1() {
        return sub_tileID_1;
    }

    public void setSub_tileID_1(String sub_tileID_1) {
        this.sub_tileID_1 = sub_tileID_1;
    }

    public String getSub_titleID_2() {
        return sub_titleID_2;
    }

    public void setSub_titleID_2(String sub_titleID_2) {
        this.sub_titleID_2 = sub_titleID_2;
    }

    public String getFlowID() {
        return flowID;
    }

    public void setFlowID(String flowID) {
        this.flowID = flowID;
    }

    public String getQuestion_set_id() {
        return question_set_id;
    }

    public void setQuestion_set_id(String question_set_id) {
        this.question_set_id = question_set_id;
    }
}
