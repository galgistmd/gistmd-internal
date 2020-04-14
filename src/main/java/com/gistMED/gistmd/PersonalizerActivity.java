package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gistMED.gistmd.Classes.Answer;
import com.gistMED.gistmd.Classes.InformationPackage;
import com.gistMED.gistmd.Classes.PersonalizerAdapter;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.gistMED.gistmd.Classes.ToolBarConfig;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PersonalizerActivity extends AppCompatActivity {

    private HashMap<String,String> collectionView = new HashMap<>();
    private ArrayList<InformationPackage> procedureInfo = new ArrayList<>();
    private String selectedSubjectID;
    private RecyclerView cardsRV;
    private PersonalizerAdapter cardsAdapter;
    private ImageButton button_next,button_back;
    private TextView sub_label;
    private String flowID;
    private int index = 0;
    CarouselView carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizer);

        ToolBarConfig.ConfigToolBar(this);

        cardsRV = findViewById(R.id.recycleview_card);
        carouselView = findViewById(R.id.carouselView);
        sub_label = findViewById(R.id.sub_label);

        selectedSubjectID = StaticObjects.selectedSubject.getID(); //TODO: GET REAL SELECTED PROCEDURE FROM USER
        StaticObjects.set.put("procedure", selectedSubjectID);

        GetCollectionViewForInformationPackage();
    }

    public void setUpTopCarouselView()
    {
        carouselView.setSize(procedureInfo.size());
        carouselView.setResource(R.layout.cardview_item_question);
        carouselView.hideIndicator(true);
        carouselView.setScaleOnScroll(true);
        carouselView.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, int position) {
                // Example here is setting up a full image carousel
                TextView tv_question = view.findViewById(R.id.text_card); //TODO: CHANGE QUESTION UI AFTER SELECTED
                tv_question.setText(procedureInfo.get(position).getLabel());
            }
        });
        // After you finish setting up, show the CarouselView
        carouselView.setCurrentItem(StaticObjects.pagePos);
        carouselView.show();
        carouselView.setCarouselScrollListener(new CarouselScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState, int position) {
                StaticObjects.pagePos = position;
                //SetUpCardsUI();
                cardsAdapter.notifyDataSetChanged();
                SetSubLabel();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    private void GetProcedureInfo(final String procedure)
    {
        final int[] i = {0};
        StaticObjects.mDataBaseRef.child("procedure_information").child(procedure).child("questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot question : dataSnapshot.getChildren())
                {
                    String questionLabelID = question.child("label").getValue(String.class);
                    String qID = question.getKey();
                    String questionSubLabelID = question.child("sub_label").getValue(String.class);
                    String viewID = question.child("view_id").getValue(String.class);

                    String questionLabel = StaticObjects.labelsTranslations.get(questionLabelID);
                    String questionSubLabel = StaticObjects.labelsTranslations.get(questionSubLabelID);

                    InformationPackage informationPackage = new InformationPackage(questionLabel,questionSubLabel,qID,viewID);

                    procedureInfo.add(informationPackage);

                    for (DataSnapshot answers : question.child("answers").getChildren())
                    {
                        String answerLabelID = answers.child("label").getValue(String.class);
                        String aID = answers.getKey();
                        String answerLabel = StaticObjects.labelsTranslations.get(answerLabelID);
                        String iconID = answers.child("icon_id").getValue(String.class);
                        procedureInfo.get(i[0]).AddAnswer(new Answer(answerLabel,iconID,aID));
                    }
                    String viewName = collectionView.get(viewID);
                    procedureInfo.get(i[0]).setCollectionView(viewName);
                    i[0]++;
                }
                setUpTopCarouselView();
           //     GetIconURL();
                SetUpCardsUI();
                SetSubLabel();

                Log.e("questions and answers",StaticObjects.printQuestionSet(procedureInfo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetCollectionViewForInformationPackage()
    {
        StaticObjects.mDataBaseRef.child("configurations").child("view_collections").child(String.valueOf(StaticObjects.selectedSubject.getViewCollectionNumber())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String key = ds.getValue(String.class);
                    String val = ds.getKey();
                    collectionView.put(key,val);
                }
                GetViewsNames();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetViewsNames()
    {
        StaticObjects.mDataBaseRef.child("configurations").child("views").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(collectionView.containsKey(ds.getKey())) {
                        String viewName = ds.child("char_name").getValue(String.class);
                        collectionView.replace(ds.getKey(),viewName);
                    }
                }
                GetProcedureInfo(selectedSubjectID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetIconURL()
    {
        for (int j = 0;j<procedureInfo.size();j++) {
            for (final int[] i = {0}; i[0] < procedureInfo.get(j).getAnswers().size(); i[0]++) {

                final Answer answerToShow = procedureInfo.get(j).getAnswers().get(i[0]);
                String childRef = "Icons" + "/" + answerToShow.getIconID() + ".png";
                StorageReference ref = StaticObjects.mStorageRef.child(childRef);

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        answerToShow.setIconURL(uri.toString());
                        if(procedureInfo.get(StaticObjects.pagePos).checkIcons()) {
                            SetUpCardsUI();
                        }
                    }
                });
            }
        }
    }

    private void SetUpCardsUI()
    {
        cardsAdapter = new PersonalizerAdapter(getApplicationContext(), procedureInfo,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        cardsRV.setLayoutManager(gridLayoutManager);
        cardsRV.setAdapter(cardsAdapter);
    }



    private void SetSubLabel()
    {
        if(sub_label!=null && procedureInfo.get(StaticObjects.pagePos).getSubLabel() != null)
            sub_label.setText(procedureInfo.get(StaticObjects.pagePos).getSubLabel());
    }

}
