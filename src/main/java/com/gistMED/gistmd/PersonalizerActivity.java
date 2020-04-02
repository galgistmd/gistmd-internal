package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gistMED.gistmd.Classes.Answer;
import com.gistMED.gistmd.Classes.InformationPackage;
import com.gistMED.gistmd.Classes.ScenarioID;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Queue;

public class PersonalizerActivity extends AppCompatActivity {

    private LinkedHashMap<String,String> blocksFromFlow = new LinkedHashMap<>();
    private LinkedHashMap<String,String> set = new LinkedHashMap<>();
    private ArrayList<InformationPackage> procedureInfo = new ArrayList<>();
    private String selectedProcedure;

    private String flowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizer);

        selectedProcedure = "procedure_1"; //TODO: GET REAL SELECTED PROCEDURE FROM USER

        GetProcedureInfo(selectedProcedure);

    }

    private void GetProcedureInfo(final String procedure)
    {
        final int[] i = {0};
        DatabaseReference procedureRef = StaticObjects.mDataBaseRef.child("procedure_information").child(procedure);
        procedureRef.child("questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot question : dataSnapshot.getChildren())
                {
                    String questionLabelID = question.child("label").getValue(String.class);
                    String qID = question.getKey();

                    String questionLabel = StaticObjects.labelsTranslations.get(questionLabelID);

                    procedureInfo.add(new InformationPackage(questionLabel,qID));

                    for (DataSnapshot answers : question.child("answers").getChildren())
                    {
                        String answerLabelID = answers.child("label").getValue(String.class);
                        String aID = answers.getKey();
                        String answerLabel = StaticObjects.labelsTranslations.get(answerLabelID);
                        String iconID = answers.child("icon_id").getValue(String.class);
                        procedureInfo.get(i[0]).AddAnswer(new Answer(answerLabel,iconID,aID));
                    }
                    i[0]++;
                }
                Log.e("questions and answers",StaticObjects.printQuestionSet(procedureInfo));

                set.put("procedure",selectedProcedure);
                set.put(procedureInfo.get(0).getID(),"answer_1");
                set.put(procedureInfo.get(1).getID(),"answer_2");
                set.put(procedureInfo.get(2).getID(),"answer_1");
                set.put(procedureInfo.get(3).getID(),"answer_1");
                set.put(procedureInfo.get(4).getID(),"answer_1");
                set.put(procedureInfo.get(5).getID(),"answer_2");
                set.put(procedureInfo.get(6).getID(),"answer_1");
                GetFlowIDbySet(set);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetFlowIDbySet(final HashMap <String,String> set)
    {
        final HashMap<String,String> DBset = new HashMap<>();
        final DatabaseReference ScenarioRef = StaticObjects.mDataBaseRef.child("scenario");
        ScenarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot ds:dataSnapshot.getChildren())
              {
                  for (DataSnapshot pare : ds.child("set").getChildren())
                  {
                      String k = pare.getKey();
                      String v = pare.getValue(String.class);
                      DBset.put(k,v);
                  }
                  Log.e("hash",DBset.toString());
                  if(DBset.equals(set))
                  {
                      GetBlocksByFlowID(ds.child("flow_id").getValue(String.class));
                      break;
                  }
              }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void GetBlocksByFlowID(String FlowID)
    {
        StaticObjects.mDataBaseRef.child("flows").child(FlowID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String k = ds.getKey();
                    String v = ds.getValue(String.class);
                    blocksFromFlow.put(k,v);
                }
                StaticObjects.mBlocksFromFlow = blocksFromFlow;
                Log.e("hashmap", blocksFromFlow.toString());
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
