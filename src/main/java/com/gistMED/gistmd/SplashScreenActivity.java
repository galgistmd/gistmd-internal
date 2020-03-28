package com.gistMED.gistmd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gistMED.gistmd.Classes.StaticObjects;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreenActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(getString(R.string.ApiKey))
                .setApplicationId(getString(R.string.AppID))
                .setDatabaseUrl(StaticObjects.DBurl)
                .build();
        FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, "second app");

        StaticObjects.mDBref = FirebaseDatabase.getInstance(secondApp).getReference();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, PersonalizerActivity.class); //TODO: START REAL FIRST ACTIVITY !
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
