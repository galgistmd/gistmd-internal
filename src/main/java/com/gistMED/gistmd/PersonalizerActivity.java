package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.gistMED.gistmd.Classes.FlowID;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PersonalizerActivity extends AppCompatActivity {

    private LinkedHashMap<String,String> BlocksFromFlow = new LinkedHashMap<>();
    private LinkedHashMap<String,String> Parameters = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizer);

        Parameters.put("Nikur","");
        Parameters.put("Gender","1");
        Parameters.put("Age","2");
        Parameters.put("Language","1");
        Parameters.put("Why","1");
        Parameters.put("What","1");
        Parameters.put("How","1");
        Parameters.put("Outcome","1");

        Log.e("hashmap",Parameters.toString());
        GetFlowByID(new FlowID(Parameters));
    }

    private void GetFlowByID(FlowID FlowID)
    {
        String ID = FlowID.GetFlowID();
        StaticObjects.mDBref.child("flows").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
               {
                   String k = ds.getKey();
                   String v = String.valueOf(ds.getValue(int.class));
                   BlocksFromFlow.put(k,v);
               }
                Log.e("hashmap",BlocksFromFlow.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
