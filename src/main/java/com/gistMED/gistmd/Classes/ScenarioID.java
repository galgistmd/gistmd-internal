package com.gistMED.gistmd.Classes;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScenarioID {

    private LinkedHashMap<String,String> parameters;
    private String scenarioID = "";

    public ScenarioID(LinkedHashMap<String,String> Parameters)
    {
        this.parameters = Parameters;
        GeneratesScenarioID();
    }

    public void GeneratesScenarioID()
    {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            this.scenarioID +=k+v;
        }
        Log.e("ID",this.scenarioID);
    }

    public String GetScenarioID()
    {
        if(this.scenarioID != null)
            return this.scenarioID;
        else
            return "Error";
    }
}
