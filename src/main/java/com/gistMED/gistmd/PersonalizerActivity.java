package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gistMED.gistmd.Classes.ScenarioID;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;

public class PersonalizerActivity extends AppCompatActivity {

    private LinkedHashMap<String,String> BlocksFromFlow = new LinkedHashMap<>();
    private LinkedHashMap<String,String> Parameters = new LinkedHashMap<>();
    private String FlowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizer);

        Parameters.put("Procedure","Nikur");
        Parameters.put("Gender","1");
        Parameters.put("Age","2");
        Parameters.put("Language","1");
        Parameters.put("Why","1");
        Parameters.put("What","1");
        Parameters.put("How","1");
        Parameters.put("OutCome","1");

        Log.e("hashmap",Parameters.toString());
        GetFlowByScenarioID(new ScenarioID(Parameters));
    }

    private void GetFlowByScenarioID(ScenarioID ID)
    {
        String id = ID.GetScenarioID();
        final DatabaseReference ScenarioRef = StaticObjects.mDBref.child("scenario").child(id);
        ScenarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               FlowID = dataSnapshot.child("flowID").getValue(String.class);
               UpdateScenarioCounter(ScenarioRef);
               GetBlocksByFlowID(FlowID);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void UpdateScenarioCounter(DatabaseReference ScenarioRef)
    {
        ScenarioRef.child("usedCounter").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                System.out.println("Transaction completed");
            }
        });
    }

    private void GetBlocksByFlowID(String FlowID)
    {
        StaticObjects.mDBref.child("flows").child(FlowID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String k = ds.getKey();
                    String v = String.valueOf(ds.getValue(int.class));
                    BlocksFromFlow.put(k,v);
                }
                StaticObjects.BlocksFromFlow =  BlocksFromFlow;
                Log.e("hashmap",BlocksFromFlow.toString());
                StartCinemaActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void StartCinemaActivity()
    {
        Intent Intent = new Intent(PersonalizerActivity.this, CinemaActivity.class);
        PersonalizerActivity.this.startActivity(Intent);
    }
}
