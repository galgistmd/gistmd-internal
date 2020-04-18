package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import com.gistMED.gistmd.Classes.MyAppApplication;
import com.gistMED.gistmd.Classes.Organization;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.gistMED.gistmd.Classes.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private MyAppApplication mApp;

    private Boolean finishedLoadingTranslation = true;
    private Boolean finishedLoadingUsers = false;
    private Boolean finishedGettingConfig= false;
    private Boolean finishedLoadingOrgs = false;
    private Boolean moveOnTry = false;

    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(getString(R.string.ApiKey))
                .setApplicationId(getString(R.string.AppID))
                .setDatabaseUrl(StaticObjects.DB_URL)
                .build();

        FirebaseApp.initializeApp(this.getApplicationContext(), options, "data_base");
        FirebaseApp secondApp = FirebaseApp.getInstance("data_base");
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);

        StaticObjects.mDataBaseRef = secondDatabase.getReference();
        StaticObjects.mStorageRef = FirebaseStorage.getInstance().getReference();
        StaticObjects.mAuthRef = FirebaseAuth.getInstance();

        GetUsers();

        if(false) { //only if user is auto signed in
            finishedLoadingTranslation = false;
            GetTranslation();
        }
        GetOrganizations();
        GetConfigurations();

        //Request();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(finishedLoadingTranslation && finishedLoadingUsers) {
                    MoveOn();
                } else
                    moveOnTry = true;
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void GetOrganizations()
    {
       StaticObjects.mDataBaseRef.child("organizations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot org:dataSnapshot.getChildren())
                {
                    String orgID = org.child("org_id").getValue(String.class);
                    String orgName = org.child("org_name").getValue(String.class);
                    String pointer = org.child("pointer").getValue(String.class);

                    Organization organization = new Organization(orgName,pointer,orgID);

                    StaticObjects.Organizations.add(organization);

                    for (DataSnapshot sub_org:org.child("sub_orgs").getChildren())
                    {
                        String suborgID = sub_org.child("suborg_id").getValue(String.class);
                        String suborgName = sub_org.child("suborg_name").getValue(String.class);
                        String subpointer = sub_org.child("pointer").getValue(String.class);
                        Organization suborganization = new Organization(suborgName,subpointer,suborgID);

                        organization.AddSuborganization(suborganization);
                    }
                }
                finishedLoadingOrgs = true;
                CheckThreadsCond();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetConfigurations()
    {
        StaticObjects.mDataBaseRef.child("translation").child("supported_languages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                   String key = ds.getKey();
                   String Value = ds.getValue(String.class);
                   StaticObjects.supportedLang.put(key,Value);
                }
                finishedGettingConfig = true;
                CheckThreadsCond();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetUsers()
    {
       StaticObjects.mDataBaseRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) //getting users that only from the specified organization
                {
                    String ID = user.getKey();
                    DataSnapshot childInfo = user.child("information");
                    String org = childInfo.child("managing_organization").getValue(String.class);
                    if(org.equals("idid")) //TODO : REAL CHOSEN ORG
                    {
                        String useFname = childInfo.child("first_name").getValue(String.class);
                        String useLname = childInfo.child("last_name").getValue(String.class);
                        String profileImgID =  childInfo.child("profile_img").getValue(String.class);
                        String userGender =  childInfo.child("user_gender").getValue(String.class);
                        String userRole =  childInfo.child("user_role").getValue(String.class);
                        String userLang =  childInfo.child("user_lang").getValue(String.class);
                        String userEmail = childInfo.child("user_email").getValue(String.class);

                        User userToAdd = new User(useFname,useLname,org,profileImgID,userGender,userLang,userRole,userEmail);
                        userToAdd.setUserID(ID);
                        users.add(userToAdd);
                    }
                }
                StaticObjects.users = users;
                finishedLoadingUsers = true;
                CheckThreadsCond();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                }
                finishedLoadingTranslation = true;
                CheckThreadsCond();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Request()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("please").push().child("name").setValue("nigger");
    }

    private void CheckThreadsCond()
    {
        if(moveOnTry&&finishedLoadingUsers&&finishedLoadingOrgs&&finishedGettingConfig&&finishedLoadingTranslation) //check if all threads finished
            MoveOn();
    }

    private void MoveOn()
    {
        while(StaticObjects.mDataBaseRef==null || StaticObjects.mAuthRef==null) { //check if connection is good with database
            Log.e("wating","db is null");
        }
            Intent mainIntent = new Intent(SplashScreenActivity.this, ChooseUserActivity.class); //TODO: START REAL FIRST ACTIVITY !
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
    }
}
