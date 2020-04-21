package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gistMED.gistmd.Classes.PickSubjectAdapter;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.gistMED.gistmd.Classes.Subject;
import com.gistMED.gistmd.Classes.ToolBarConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PickSubjectActivity extends AppCompatActivity {

    private RecyclerView cardsRV;

    private  ArrayList<Subject> subjectslist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_subject);

        ToolBarConfig.ConfigToolBar(this);

        cardsRV = findViewById(R.id.recycleview_card);

        GetSubjects(); //Thread To Get Subjects from DB in background
    }

    private void TranslateSubjectLabels()
    {
        for (Subject subject: subjectslist)
        {
            String label = StaticObjects.labelsTranslations.get(subject.getLabelID());
            if(label!=null)
                subject.setLabel(label);
        }
        UpdateUI();
    }


    private void GetSubjects()
    {
        StaticObjects.mDataBaseRef.child("procedure_information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot subject: dataSnapshot.getChildren())
                {
                    String labelID = subject.child("label").getValue(String.class);
                    String viewCollection = subject.child("view_collection").getValue(String.class);
                    String ID = subject.getKey();
                    String iconID = subject.child("icon_id").getValue(String.class);
                    Subject subjectOBJ = new Subject(ID,labelID,iconID,viewCollection);
                    subjectslist.add(subjectOBJ);
                }
                TranslateSubjectLabels();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void UpdateUI() {
        PickSubjectAdapter cardsAdapter = new PickSubjectAdapter(getApplicationContext(),subjectslist,this); //Set the UI
        cardsRV.setLayoutManager(new GridLayoutManager(this, 3));
        cardsRV.setAdapter(cardsAdapter);
    }
}
