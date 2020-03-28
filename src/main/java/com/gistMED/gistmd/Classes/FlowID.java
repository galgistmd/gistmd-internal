package com.gistMED.gistmd.Classes;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlowID {

    private LinkedHashMap<String,String> Parameters;
    private String FlowID = "";

    public FlowID(LinkedHashMap<String,String> Parameters)
    {
        this.Parameters = Parameters;
        GenerateFlowID();
    }

    public void GenerateFlowID()
    {
        for (Map.Entry<String, String> entry : Parameters.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            this.FlowID +=k+v;
        }
        Log.e("ID",this.FlowID);
    }

    public String GetFlowID()
    {
        if(this.FlowID != null)
            return this.FlowID;
        else
            return "Error";
    }
}
