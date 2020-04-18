package com.gistMED.gistmd.Classes;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.jama.carouselview.CarouselView;

import java.security.Timestamp;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;

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

    public static String convertTimeStampToDate(long time){
        Date dateObj = new Date(time*1000);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String text = df.format(dateObj);
        return text;
    }

    public static String convertTimeStampToTime(long time){
        Time dateObj = new Time(time*1000);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String text = df.format(dateObj);
        return text;
    }


}
