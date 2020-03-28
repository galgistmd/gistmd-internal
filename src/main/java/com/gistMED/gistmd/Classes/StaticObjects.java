package com.gistMED.gistmd.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;

public class StaticObjects {
    public static DatabaseReference mDBref;
    public static String DBurl = "https://gist-25f32-1e2ba.firebaseio.com/";
    public static LinkedHashMap<String, String> BlocksFromFlow;
}
