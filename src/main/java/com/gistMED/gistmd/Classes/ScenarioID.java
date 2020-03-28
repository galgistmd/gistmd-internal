package com.gistMED.gistmd.Classes;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScenarioID {

    private LinkedHashMap<String,String> Parameters;
    private String ScenarioID = "";

    public ScenarioID(LinkedHashMap<String,String> Parameters)
    {
        this.Parameters = Parameters;
        GeneratesScenarioID();
    }

    public void GeneratesScenarioID()
    {
        for (Map.Entry<String, String> entry : Parameters.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            this.ScenarioID +=k+v;
        }
        Log.e("ID",this.ScenarioID);
    }

    public String GetScenarioID()
    {
        if(this.ScenarioID != null)
            return this.ScenarioID;
        else
            return "Error";
    }
}
