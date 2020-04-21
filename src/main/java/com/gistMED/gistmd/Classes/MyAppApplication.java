package com.gistMED.gistmd.Classes;

import android.app.Application;

import com.gistMED.gistmd.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyAppApplication extends Application {

    public static FirebaseDatabase mGlobalDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        /*FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(getString(R.string.ApiKey))
                .setApplicationId(getString(R.string.AppID))
                .setDatabaseUrl(StaticObjects.DB_URL)
                .build();

        FirebaseApp.initializeApp(this.getApplicationContext(), options, "data_base");
        FirebaseApp secondApp = FirebaseApp.getInstance("data_base");
        mGlobalDataBase = FirebaseDatabase.getInstance(secondApp);*/
    }

}
