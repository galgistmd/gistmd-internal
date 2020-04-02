package com.gistMED.gistmd.Classes;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class StaticObjects {
    public static DatabaseReference mDataBaseRef;
    public static final String DB_URL = "https://gist-25f32-1e2ba.firebaseio.com/";
    public static LinkedHashMap<String, String> mBlocksFromFlow;
    public static HashMap<String,String> labelsTranslations = new HashMap<String,String>();

    public static String printQuestionSet(ArrayList<InformationPackage> informationPackages)
    {
        String all = "";
        for (InformationPackage informationPackage : informationPackages) {
            all+= informationPackage.getLabel()+ " :";
            for (Answer answer: informationPackage.getAnswers()) {
                all+=answer.getLabel() + ",";
            }
        }
        return all;
    }
}
