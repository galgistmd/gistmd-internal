package com.gistMED.gistmd.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class Case {

    private String comments_id;
    private String scenario_id;
    private long timestamp;
    private ArrayList<String> tagged_users = new ArrayList<>();
    private String creator_id;
    private String condition;
    private String answers_id;
    private Scenario scenario;
    private HashMap<String,String> answers = new HashMap<>();

    public Case(String comments_id, String scenario_id,String creator_id,String condition) {
        this.comments_id = comments_id;
        this.scenario_id = scenario_id;
        this.tagged_users.add(creator_id);
        this.creator_id = creator_id;
        this.condition = condition;
    }

    public HashMap<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, String> answers) {
        if(answers!=null)
            this.answers = answers;
    }

    public String getAnswers_id() {
        return answers_id;
    }

    public void setAnswers_id(String answers_id) {
        this.answers_id = answers_id;
    }

    public Case(String comments_id, String scenario_id, ArrayList<String> tagged_users, String creator_id, long timestamp, String condition, String answers_id) {
        this.comments_id = comments_id;
        this.scenario_id = scenario_id;
        this.tagged_users = tagged_users;
        this.creator_id = creator_id;
        this.timestamp = timestamp;
        this.condition = condition;
        this.answers_id = answers_id;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Case()
    {

    }

    public enum Condition
    {
        hospitalized,switchedDepartment,releasedHome;
    }

    public String getComments_id() {
        return comments_id;
    }

    public void setComments_id(String comments_id) {
        this.comments_id = comments_id;
    }

    public String getScenario_id() {
        return scenario_id;
    }

    public void setScenario_id(String scenario_id) {
        this.scenario_id = scenario_id;
    }

    public ArrayList<String> getTagged_users() {
        return tagged_users;
    }

    public void setTagged_users(ArrayList<String> tagged_users) {
        this.tagged_users = tagged_users;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }
}
