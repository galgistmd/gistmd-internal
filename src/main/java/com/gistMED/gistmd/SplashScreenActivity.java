package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.gistMED.gistmd.Classes.StaticObjects;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private Boolean finishedLoadingTranslation = false;
    private Boolean moveOnTry = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(getString(R.string.ApiKey))
                .setApplicationId(getString(R.string.AppID))
                .setDatabaseUrl(StaticObjects.DB_URL)
                .build();
        FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, "second app");

        StaticObjects.mDataBaseRef = FirebaseDatabase.getInstance(secondApp).getReference();

        GetTranslation();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(finishedLoadingTranslation)
                  MoveOn();
                else
                    moveOnTry = true;
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void GetTranslation() {
        StaticObjects.mDataBaseRef.child("translation").child("label_translations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    String Value = child.child("lang_he").getValue(String.class);
                    StaticObjects.labelsTranslations.put(key,Value);
                    if(moveOnTry)
                        MoveOn();
                }
                finishedLoadingTranslation = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void MoveOn()
    {
        Intent mainIntent = new Intent(SplashScreenActivity.this, PersonalizerActivity.class); //TODO: START REAL FIRST ACTIVITY !
        SplashScreenActivity.this.startActivity(mainIntent);
        SplashScreenActivity.this.finish();
    }
}
