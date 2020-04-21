package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gistMED.gistmd.Classes.Case;
import com.gistMED.gistmd.Classes.CaseQuestion;
import com.gistMED.gistmd.Classes.Scenario;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.gistMED.gistmd.Classes.ToolBarConfig;
import com.gistMED.gistmd.Classes.UpdateUserInfoActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPageActivity extends AppCompatActivity {

    private ListView cases_listview;
    private ArrayList<Case> casesToShow = new ArrayList<>();
    private ImageButton updateinfo_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page_activity);

        cases_listview = findViewById(R.id.cases_listview);
        updateinfo_button = findViewById(R.id.updateinfo_button);

        ToolBarConfig.ConfigToolBar(this);

        CircleImageView circleImageView = findViewById(R.id.img_view_profile);
        Glide.with(getApplicationContext())
                .load(StaticObjects.mConnecteduserinfo.getProfileImgURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);

        TextView tv_name_txtview = findViewById(R.id.tv_name_txtview);
        tv_name_txtview.setText(StaticObjects.mConnecteduserinfo.getFirst_name()+" "+StaticObjects.mConnecteduserinfo.getLast_name());

        GetCases();
        //GetCasesTry(); TODO://GOOF FUNC FOR FUTURE

        updateinfo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserInfoActivity dialog = new UpdateUserInfoActivity(UserPageActivity.this,getApplicationContext());
                dialog.show();
            }
        });
    }

    private void GetCases()
    {
        StaticObjects.mDataBaseRef.child("cases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Case:dataSnapshot.getChildren()) {
                    for (DataSnapshot taggedUsers : Case.child("tagged_users").getChildren())
                    {
                        if(taggedUsers.getValue(String.class).equals(StaticObjects.mConnecteduserinfo.getUserID()))
                        {
                            String commentsID = Case.child("comment_section_id").getValue(String.class);
                            String scenario_id = Case.child("scenario_id").getValue(String.class);
                            ArrayList<String> tagged_users = (ArrayList<String>) Case.child("tagged_users").getValue();
                            long timestamp = Case.child("timestamp").getValue(long.class);
                            String condition = Case.child("condition").getValue(String.class);
                            String creatorID = Case.child("creator_id").getValue(String.class);

                            String answers_id = null;

                            if(Case.child("answers_id").exists()) {
                                answers_id = Case.child("answers_id").getValue(String.class);
                            }

                            Case CaseToAdd = new Case(commentsID,scenario_id,tagged_users,creatorID,timestamp,condition,answers_id);

                            if(answers_id!=null)
                                GetAnswers(CaseToAdd);

                            else
                                getScenario(CaseToAdd);


                            casesToShow.add(CaseToAdd);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void GetAnswers(final Case Case)
    {
        StaticObjects.mDataBaseRef.child("answers").child(Case.getAnswers_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Case.setAnswers((HashMap<String, String>) dataSnapshot.getValue());
                Log.e("hashhash",Case.getAnswers().toString());
                getScenario(Case);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*    private void GetCasesTry()
    {
        Query test = StaticObjects.mDataBaseRef.child("cases").orderByChild("tagged_user_forme/"+StaticObjects.mConnecteduserinfo.getUserID()).equalTo(true);

        test.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Case:dataSnapshot.getChildren()) {
                            String commentsID = Case.child("comment_section_id").getValue(String.class);
                            String scenario_id = Case.child("scenario_id").getValue(String.class);
                            ArrayList<String> tagged_users = (ArrayList<String>) Case.child("tagged_users").getValue();
                            long timestamp = Case.child("timestamp").getValue(long.class);
                            String condition = Case.child("condition").getValue(String.class);
                            String creatorID = Case.child("creator_id").getValue(String.class);

                            String answers_id = null;

                            if(Case.child("answers_id").exists())
                                answers_id = Case.child("answers_id").getValue(String.class);

                            Case CaseToAdd = new Case(commentsID,scenario_id,tagged_users,creatorID,timestamp,condition,answers_id);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void getScenario(final Case CaseToAdd)
    {
            final String scenarioID = CaseToAdd.getScenario_id();

            StaticObjects.mDataBaseRef.child("scenario").child(scenarioID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String title = dataSnapshot.child("description").child("title").getValue(String.class);
                    String sub_title_1 = dataSnapshot.child("description").child("sub_title_1").getValue(String.class);
                    String sub_title_2 = dataSnapshot.child("description").child("sub_title_2").getValue(String.class);
                    String flowID = dataSnapshot.child("flow_id").getValue(String.class);
                    String question_set_id = dataSnapshot.child("question_set_id").getValue(String.class);
                    String iconID = dataSnapshot.child("description").child("icon_id").getValue(String.class);

                    Scenario scenario = new Scenario(scenarioID,title,sub_title_1,sub_title_2,flowID,question_set_id,iconID);
                    CaseToAdd.setScenario(scenario);
                    GetQuestionSet(scenario);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void GetQuestionSet(final Scenario scenario)
    {
        StaticObjects.mDataBaseRef.child("question_set").child(scenario.getQuestion_set_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionid :dataSnapshot.getChildren())
                {
                    AddQuestionToScenario(scenario,questionid.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddQuestionToScenario(final Scenario scenario, String questionID)
    {
        StaticObjects.mDataBaseRef.child("question_stock").child(questionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String answerType = dataSnapshot.child("answer_type").getValue(String.class);

                String labelID = dataSnapshot.child("label_id").getValue(String.class);
                String questionID = dataSnapshot.getKey();
                CaseQuestion caseQuestion = new CaseQuestion(labelID,answerType,questionID);

                scenario.setCaseQuestions(caseQuestion);
                SetUIlistview();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetUIlistview()
    {
        cases_listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return casesToShow.size();
            }

            @Override
            public Case getItem(int case_position) {
                return casesToShow.get(case_position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int case_position, View convertView, ViewGroup parent) {

                View view = getLayoutInflater().inflate(R.layout.case_listview_layout,null);

                TextView proc_type_txtview = view.findViewById(R.id.proc_type_txtview);
                TextView age_txtview = view.findViewById(R.id.age_txtview);
                final TextView lang_type = view.findViewById(R.id.lang_type);
                final ImageView img_view_icon = view.findViewById(R.id.img_view_icon);
                TextView date_txtview = view.findViewById(R.id.date_txtview);
               TextView time_txtview = view.findViewById(R.id.time_txtview);
                final ImageView expand_donts = view.findViewById(R.id.expand_donts);
                final ListView listView = view.findViewById(R.id.questions_listview);

                String date = StaticObjects.convertTimeStampToDate(getItem(case_position).getTimestamp());
                String time = StaticObjects.convertTimeStampToTime(getItem(case_position).getTimestamp());


                        listView.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                int size = casesToShow.get(case_position).getScenario().getCaseQuestions().size();
                                return size;
                            }

                            @Override
                            public CaseQuestion getItem(int position) {
                                return casesToShow.get(case_position).getScenario().getCaseQuestions().get(position);
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                View view;
                                TextView question_textview;
                                final ImageButton check_button,x_button;
                                String qusetion = StaticObjects.labelsTranslations.get(getItem(position).getLabelID());
                                final Case CurrentCase = casesToShow.get(case_position);
                                if(getItem(position).getAnswerType().equals("yesNo"))
                                {
                                    view = getLayoutInflater().inflate(R.layout.yesno_question_listview,null);
                                    question_textview = view.findViewById(R.id.question_textview);
                                    check_button = view.findViewById(R.id.check_button);
                                    x_button = view.findViewById(R.id.x_button);

                                    x_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            x_button.setBackground(getResources().getDrawable(R.drawable.xselected));
                                            check_button.setBackground(getResources().getDrawable(R.drawable.checkbuttonnotselected));
                                            String selection = "1";
                                                if (CurrentCase.getAnswers().containsKey(getItem(position).getQuestionID())) {
                                                    CurrentCase.getAnswers().replace(getItem(position).getQuestionID(), selection);
                                                }
                                            else
                                                CurrentCase.getAnswers().put(getItem(position).getQuestionID(),selection);
                                            StaticObjects.mDataBaseRef.child("answers").child(CurrentCase.getAnswers_id()).child(getItem(position).getQuestionID()).setValue(selection);

                                        }
                                    });

                                    check_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            check_button.setBackground(getResources().getDrawable(R.drawable.checkselected));
                                            x_button.setBackground(getResources().getDrawable(R.drawable.nobuttonunselected));
                                            String selection = "0";
                                                if (CurrentCase.getAnswers().containsKey(getItem(position).getQuestionID())) {
                                                    CurrentCase.getAnswers().replace(getItem(position).getQuestionID(), selection);
                                            }
                                            else
                                                CurrentCase.getAnswers().put(getItem(position).getQuestionID(),selection);
                                            StaticObjects.mDataBaseRef.child("answers").child(CurrentCase.getAnswers_id()).child(getItem(position).getQuestionID()).setValue(selection);
                                        }
                                    });

                                    if(CurrentCase.getAnswers()!=null) {
                                        if (CurrentCase.getAnswers().containsKey(getItem(position).getQuestionID())) {

                                            int selectedButtonIndex = Integer.valueOf(CurrentCase.getAnswers().get(getItem(position).getQuestionID()));

                                            switch (selectedButtonIndex) {
                                                case 0:
                                                    check_button.setBackground(getResources().getDrawable(R.drawable.checkselected));
                                                    x_button.setBackground(getResources().getDrawable(R.drawable.nobuttonunselected));
                                                    break;
                                                case 1:
                                                    x_button.setBackground(getResources().getDrawable(R.drawable.xselected));
                                                    check_button.setBackground(getResources().getDrawable(R.drawable.checkbuttonnotselected));
                                            }
                                        }
                                    }

                                    question_textview.setText(qusetion);
                                }
                                else
                                {
                                    view = getLayoutInflater().inflate(R.layout.slider_question_listview,null);
                                    IndicatorSeekBar slider = view.findViewById(R.id.slider);

                                    slider.setOnSeekChangeListener(new OnSeekChangeListener() {
                                        @Override
                                        public void onSeeking(SeekParams seekParams) {
                                            if (CurrentCase.getAnswers().containsKey(getItem(position).getQuestionID())) {
                                                CurrentCase.getAnswers().replace(getItem(position).getQuestionID(),String.valueOf(seekParams.progress));
                                            }
                                            else
                                            {
                                                CurrentCase.getAnswers().put(getItem(position).getQuestionID(),String.valueOf(seekParams.progress));
                                            }
                                            StaticObjects.mDataBaseRef.child("answers").child(CurrentCase.getAnswers_id()).child(getItem(position).getQuestionID()).setValue(String.valueOf(seekParams.progress));
                                        }

                                        @Override
                                        public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                                        }
                                    });

                                    if(CurrentCase.getAnswers()!=null) {
                                        if (CurrentCase.getAnswers().containsKey(getItem(position).getQuestionID())) {
                                            float prog = Float.parseFloat(CurrentCase.getAnswers().get(getItem(position).getQuestionID()));
                                            slider.setProgress(prog);
                                        }
                                    }
                                    question_textview = view.findViewById(R.id.question_textview);
                                    question_textview.setText(qusetion);

                                }
                                return view;
                            }
                        });

                expand_donts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(expand_donts.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.expanddonts).getConstantState()) {
                            AnimateListView(1000,listView);
                            listView.setNestedScrollingEnabled(true);
                            SendNotification();
                            expand_donts.setImageResource(R.drawable.closeicon);
                        }
                        else {
                            AnimateListView(-1000,listView);
                            listView.setNestedScrollingEnabled(false);
                            expand_donts.setImageResource(R.drawable.expanddonts);
                        }
                    }
                });


                proc_type_txtview.setText(StaticObjects.labelsTranslations.get(getItem(case_position).getScenario().getTitleID()));
                age_txtview.setText("גיל "+StaticObjects.labelsTranslations.get(getItem(case_position).getScenario().getSub_tileID_1()));
                lang_type.setText(StaticObjects.labelsTranslations.get(getItem(case_position).getScenario().getSub_titleID_2()));
                date_txtview.setText(date);
                time_txtview.setText(time);

                if(getItem(case_position).getScenario().getIconURL()!=null)
                {
                    Glide.with(getApplicationContext())
                            .load(getItem(case_position).getScenario().getIconURL())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img_view_icon);
                }

                else {
                    String childRef = "Icons" + "/" + getItem(case_position).getScenario().getIconID() + ".png";
                    StorageReference ref = StaticObjects.mStorageRef.child(childRef);
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getItem(case_position).getScenario().setIconURL(uri.toString());
                            Glide.with(getApplicationContext())
                                    .load(getItem(case_position).getScenario().getIconURL())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(img_view_icon);
                        }
                    });
                }


                return view;
            }

        });
    }

    private void SendNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json,application/json");
                    List<String> a = new ArrayList<>();
            //        new Gson().toList(a); // ["a1", "a2"]
                    RequestBody body = RequestBody.create(mediaType,
                            String.format("{ \r\n    \"notification\": {\r\n        \"title\": \"%s\",\r\n        \"text\": \"%s\",\r\n    },\r\n    \"to\":\"%s\"\r\n}",
                                    StaticObjects.mConnecteduserinfo.getFirst_name(), "Notification body", "dRziD9zu-N4:APA91bEZvIEZJ8EBynSIP1_nophuOz7iHr0nBgWCZfdYDYht4A3GnMvOEAFKt3nPJMa61D8tDgpLETDY3FGyQzbzqrsTZT1DbMYXQ2ol4D4558PV4gam7scU0EcapW0FCA1uqQ4NCcfV"));
                    Request request = new Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "key=AAAAkZA4q-Y:APA91bH4J6PW_3ab7vaAxyW3K9c2sYl7yMgkuVpdfbMmhjhxdAt-VzTKcFNT907qXo8BcBLSRci_jt9iKFGA_RoaBGkMvgSeeMa8D5P2Y4DbiwDIjn8r6H41nycssDsbpAL_g8azhiFx")
                            .build();
                    Response response = client.newCall(request).execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void AnimateListView(int direction, final ListView listViewToAnimate)
    {
        ValueAnimator anim = ValueAnimator.ofInt(listViewToAnimate.getMeasuredHeight(),direction);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = listViewToAnimate.getLayoutParams();
                layoutParams.height = val;
                listViewToAnimate.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
    }
}
