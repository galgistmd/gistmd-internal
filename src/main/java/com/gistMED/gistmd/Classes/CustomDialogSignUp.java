package com.gistMED.gistmd.Classes;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.gistMED.gistmd.PickSubjectActivity;
import com.gistMED.gistmd.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomDialogSignUp extends Dialog{

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

    private Spinner spinner_2;

    private CardView cardview;
    private LinearLayout layout_start;
    private LinearLayout finish_layout;
    private LinearLayout loading_layout;

    private Uri userProfileImg;

    private String userFname,userLname,userEmail,userGender,userRole,userOrg,userLang;

    private List<String> supportedLang  = new ArrayList<String>();


    private Boolean validFname = false,validLname = false ,validEmail = false,validGender = false,validRole =false,validPassword = false,validConfrimPassword=false,validLang = false;


    private AVLoadingIndicatorView progress_bar;

    public CustomDialogSignUp(Activity a,Context mContext) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_user_dialog);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        signup_button = findViewById(R.id.signup_button);
        profile_image = findViewById(R.id.profile_image);

        txt_view = findViewById(R.id.txt_view);
        txt_view_2 = findViewById(R.id.txt_view_2);

        lname_editxt = findViewById(R.id.lname_editxt);
        fname_editxt = findViewById(R.id.fname_editxt);
        email_editxt = findViewById(R.id.email_editxt);

        select_gender = findViewById(R.id.select_gender);
        select_role = findViewById(R.id.select_role);

        cardview =findViewById(R.id.cardview);

        layout_start = findViewById(R.id.layout_start);
        finish_layout = findViewById(R.id.finish_layout);
        loading_layout = findViewById(R.id.loading_layout);

        profile_image = findViewById(R.id.profile_image);

        signup_button.setEnabled(false);

        TextWatchers();

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimateCard();
            }
        });

        select_gender.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                validGender = true;
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
                validRole = true;
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

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
            }
        });
    }

    private void TextWatchersview2()
    {
        password_editxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>=6) {
                    validPassword = true;
                }
                else
                {
                    validPassword = false;
                }

                validConfrimPassword = DoesPasswordsMatch(s.toString(),confrim_password_editxt.getText().toString());
                TakeCareOfButtonSignUp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confrim_password_editxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validConfrimPassword = DoesPasswordsMatch(s.toString(),confrim_password_editxt.getText().toString());
                TakeCareOfButtonSignUp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private Boolean DoesPasswordsMatch(String pass1,String pass2)
    {
        if(pass1.equals(pass2))
            return true;
        else
            return false;
    }

    private void SetSecondView()
    {
        spinner_2 = findViewById(R.id.spinner_2);
        signup_button_2 = findViewById(R.id.signup_button_2);
        password_editxt=findViewById(R.id.confrim_password_editxt);
        confrim_password_editxt = findViewById(R.id.confrim_password_editxt);

        TextWatchersview2();

        signup_button_2.setEnabled(false);

        SetUpSpinners();

        signup_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                if(selection.equals(" ")) {
                    validLang = false;
                    TakeCareOfButtonSignUp();
                }
                else
                {
                    userLang = StaticObjects.supportedLang.get(selection);
                    validLang = true;
                    TakeCareOfButtonSignUp();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void signUp()
    {
        AnimateLoading();
        StaticObjects.mAuthRef.createUserWithEmailAndPassword(userEmail,password_editxt.getText().toString())
                .addOnCompleteListener(c, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String uuid = StaticObjects.mDataBaseRef.child("users").push().getKey();
                            User user = new User(userFname,userLname,"idid",uuid,userGender,userLang,userRole,userEmail);
                            StaticObjects.mDataBaseRef.child("users").child(uuid).child("information").setValue(user);
                            GetTranslation(user.getUser_lang());
                            StaticObjects.mConnecteduserinfo = user;
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
    }

    private void GetTranslation(final String lang) {
        final CustomDialogSignUp dialog = this;
        StaticObjects.mDataBaseRef.child("translation").child("label_translations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    String Value = child.child("lang_"+lang).getValue(String.class);
                    StaticObjects.labelsTranslations.put(key,Value);
                }
                dialog.dismiss();
                c.startActivity(new Intent(c, PickSubjectActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AnimateLoading()
    {
        finish_layout.animate().translationX(-1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                loading_layout.setVisibility(View.VISIBLE);
                finish_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void AnimateCard()
    {
        layout_start.animate().translationX(-1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                GetInfo();
                finish_layout.setVisibility(View.VISIBLE);
                layout_start.setVisibility(View.GONE);
                SetSecondView();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void SetUpSpinners()
    {
        for (Map.Entry<String,String> entry: StaticObjects.supportedLang.entrySet()) {
                supportedLang.add(entry.getKey());
        }

        supportedLang.add(0," ");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_item,supportedLang);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(dataAdapter);
    }

    private void GetInfo()
    {
        userFname = fname_editxt.getText().toString();
        userLname = lname_editxt.getText().toString();
        userEmail = email_editxt.getText().toString();
       // userProfileImg = profile_image.
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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1)
                    validLname = true;
                else
                    validLname = false;
                TakeCareOfButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void TakeCareOfButton()
    {
        if(validEmail&&validFname&&validLname&&validGender&&validRole)
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

    private void TakeCareOfButtonSignUp()
    {
        if(validLang&&validPassword&&validConfrimPassword)
        {
            signup_button_2.setBackgroundResource(R.drawable.rounded_enabled_button);
            signup_button_2.setTextColor(Color.WHITE);
            signup_button_2.setEnabled(true);
        }
        else
        {
            signup_button_2.setBackgroundResource(R.drawable.button_unenabled);
            signup_button_2.setTextColor(Color.parseColor("#636e72"));
            signup_button_2.setEnabled(false);
        }
    }
}
