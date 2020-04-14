package com.gistMED.gistmd.Classes;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.jama.carouselview.CarouselView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class StaticObjects {
    public static DatabaseReference mDataBaseRef;
    public static final String DB_URL = "https://gist-25f32-1e2ba.firebaseio.com/";
    public static LinkedHashMap<String,String> set = new LinkedHashMap<>();
    public static HashMap<String,String> labelsTranslations = new HashMap<String,String>();
    public static StorageReference mStorageRef;
    public static int pagePos = 0;
    public static Subject selectedSubject;
    public static ArrayList<User> users;
    public static FirebaseAuth mAuthRef;
    public static HashMap<String,String> supportedLang = new HashMap<>();
    public static ArrayList<Organization> Organizations = new ArrayList<>();
    public static User mConnecteduserinfo;

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
