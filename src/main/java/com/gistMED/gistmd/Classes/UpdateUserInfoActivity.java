package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.gistMED.gistmd.R;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserInfoActivity extends Dialog {

    private Activity c;
    private Context mContext;

    private Button signup_button,signup_button_2;

    private CircleImageView profile_image;

    private TextView txt_view;
    private TextView txt_view_1;
    private TextView txt_view_2;

    private EditText fname_editxt;
    private EditText lname_editxt;
    private EditText email_editxt;
    private EditText password_editxt;
    private EditText confrim_password_editxt;

    private RadioRealButtonGroup select_gender;
    private RadioRealButtonGroup select_role;

    private RadioRealButton woman,man,doctor,nurse;

    private Spinner spinner_2;

    private CardView cardview;
    private LinearLayout layout_start;
    private LinearLayout finish_layout;
    private LinearLayout loading_layout;

    private Uri userProfileImg;

    private String userFname,userLname,userEmail,userGender,userRole,userOrg,userLang;

    private List<String> supportedLang  = new ArrayList<String>();


    private Boolean validFname = true,validLname = true ,validEmail = true;

    public UpdateUserInfoActivity(Activity a,Context mContext) {
        super(a);

        this.c = a;
        this.mContext = mContext;

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_user_dialog);

        signup_button = findViewById(R.id.signup_button);
        profile_image = findViewById(R.id.profile_image);

        lname_editxt = findViewById(R.id.lname_editxt);
        fname_editxt = findViewById(R.id.fname_editxt);
        email_editxt = findViewById(R.id.email_editxt);

        select_gender = findViewById(R.id.select_gender);
        select_role = findViewById(R.id.select_role);

        woman = findViewById(R.id.woman);
        man = findViewById(R.id.man);

        doctor = findViewById(R.id.doctor);
        nurse = findViewById(R.id.nurse);

        cardview =findViewById(R.id.cardview);

        layout_start = findViewById(R.id.layout_start);
        finish_layout = findViewById(R.id.finish_layout);
        loading_layout = findViewById(R.id.loading_layout);

        profile_image = findViewById(R.id.profile_image);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFname = fname_editxt.getText().toString();
                userLname = lname_editxt.getText().toString();
                userEmail = email_editxt.getText().toString();
                StaticObjects.mConnecteduserinfo.setFirst_name(userFname);
                StaticObjects.mConnecteduserinfo.setLast_name(userLname);
                StaticObjects.mConnecteduserinfo.setUser_email(userEmail);
                StaticObjects.mConnecteduserinfo.setUser_gender(userGender);
                StaticObjects.mConnecteduserinfo.setUser_role(userRole);
                StaticObjects.mDataBaseRef.child("users").child(StaticObjects.mConnecteduserinfo.getUserID()).child("information").setValue(StaticObjects.mConnecteduserinfo);
            }
        });


        select_gender.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                switch(position) {
                    case 0:
                        userGender = User.Gender.Female.toString();
                        break;
                    case 1:
                        userGender = User.Gender.Male.toString();
                        break;
                    default:
                        // code block
                }
                TakeCareOfButton();
            }
        });

        select_role.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                switch(position) {
                    case 0:
                        userRole = User.Role.Doctor.toString();
                        break;
                    case 1:
                        userRole = User.Role.Nurse.toString();
                        break;
                    default:
                        // code block
                }
                TakeCareOfButton();
            }
        });

        ShowActiveuserInfo();
    }

    private void ShowActiveuserInfo()
    {
        fname_editxt.setText(StaticObjects.mConnecteduserinfo.getFirst_name());
        lname_editxt.setText(StaticObjects.mConnecteduserinfo.getLast_name());
        email_editxt.setText(StaticObjects.mConnecteduserinfo.getUser_email());

        TextWatchers();



        if(StaticObjects.mConnecteduserinfo.getUser_gender().equals(User.Gender.Male)) {
            select_gender.setPosition(1);
            userGender = User.Gender.Male.toString();
        }
        else {
            select_gender.setPosition(0);
            userGender = User.Gender.Female.toString();
        }
        if (StaticObjects.mConnecteduserinfo.getUser_gender().equals(User.Role.Doctor)) {
            select_role.setPosition(0);
            userGender = User.Role.Doctor.toString();
        }
        else {
            select_role.setPosition(1);
            userGender = User.Role.Nurse.toString();
        }
    }

    private void TextWatchers()
    {
        email_editxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = email_editxt.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(email.matches(emailPattern)  && s.length() > 0) {
                    validEmail = true;

                }
                else
                    validEmail = false;
                TakeCareOfButton();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        fname_editxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1)
                    validFname = true;
                else
                    validFname = false;
                TakeCareOfButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lname_editxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() > 1)
                    validLname = true;
                else
                    validLname = false;
                TakeCareOfButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void TakeCareOfButton()
    {
        if(validEmail&&validFname&&validLname)
        {
            signup_button.setBackgroundResource(R.drawable.rounded_enabled_button);
            signup_button.setTextColor(Color.WHITE);
            signup_button.setEnabled(true);
        }
        else
        {
            signup_button.setBackgroundResource(R.drawable.button_unenabled);
            signup_button.setTextColor(Color.parseColor("#636e72"));
            signup_button.setEnabled(false);
        }
    }
}
