package com.gistMED.gistmd.Classes;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wang.avi.AVLoadingIndicatorView;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialogSignIn extends Dialog{

    private Activity c;
    private Context mContext;

    private LinearLayout enterpass_layout;
    private LinearLayout layout_loading;

    private Button signin_button;

    private EditText password_edit_txt;
    private TextView welcome_tv;

    private User selectedUser;

    private CardView cardView;
    private AVLoadingIndicatorView progress_bar;

    public CustomDialogSignIn(Activity a, User selectedUser, Context mContext) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.selectedUser = selectedUser;
        this.mContext = mContext;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_sign_in);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        signin_button = findViewById(R.id.signin_button);
        password_edit_txt = findViewById(R.id.password_edit_txt);
        welcome_tv = findViewById(R.id.welcome_tv);
        cardView = findViewById(R.id.cardview);
        progress_bar = findViewById(R.id.progress_bar);
        enterpass_layout = findViewById(R.id.enterpass_layout);
        layout_loading = findViewById(R.id.layout_loading);

        String usersName = selectedUser.getFirst_name() + " " +selectedUser.getLast_name();

        welcome_tv.setText("אהלן " + usersName +" !");

        password_edit_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TakeCareOfLogInButton(IsPassWordValid(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInWithEmailAndPassword(selectedUser.getUser_email(),password_edit_txt.getText().toString());
            }
        });
    }

    private void SignInWithEmailAndPassword(String email,String password)
    {
        CardAnimation();
        StaticObjects.mAuthRef.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.c, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GetUsersTranslation(selectedUser.getUser_lang());
                            StaticObjects.mConnecteduserinfo = selectedUser; //TODO:CHANGE WITH SHARED PREF
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    StaticObjects.mConnecteduserinfo.setToken(task.getResult().getToken());
                                    StaticObjects.mDataBaseRef.child("users").child(selectedUser.getUserID()).child("information").child("token").setValue(StaticObjects.mConnecteduserinfo.getToken());
                                }
                            });
                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            //sign in fail
                        }
                    }
                });
    }

    private void fadeOutAndHideLayout(final CardView linearLayout)
    {

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);
        progress_bar.setVisibility(View.VISIBLE);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                linearLayout.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        linearLayout.startAnimation(fadeOut);
    }


    private void CardAnimation()
    {
        enterpass_layout.animate().translationX(-1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layout_loading.setVisibility(View.VISIBLE);
                enterpass_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void GetUsersTranslation(final String usersLang)
    {
        final CustomDialogSignIn customDialogSignIn = this;
        StaticObjects.mDataBaseRef.child("translation").child("label_translations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    String Value = child.child("lang_"+usersLang).getValue(String.class);
                    StaticObjects.labelsTranslations.put(key,Value);
                }
                c.startActivity(new Intent(c, PickSubjectActivity.class));
                customDialogSignIn.dismiss();
                c.finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UpdateUIWithProgress() {
        signin_button.setEnabled(false);
        signin_button.setVisibility(View.GONE);
        welcome_tv.setVisibility(View.GONE);
        password_edit_txt.setEnabled(false);
        password_edit_txt.setVisibility(View.GONE);
        progress_bar.show();
    }

    private Boolean IsPassWordValid(CharSequence password)
    {
        if(password.length() >= 6)
            return true;
        else
            return false;
    }

    private void TakeCareOfLogInButton(Boolean validInput)
    {
        if(validInput)
        {
            signin_button.setBackgroundResource(R.drawable.rounded_enabled_button);
            signin_button.setTextColor(Color.WHITE);
            signin_button.setEnabled(true);
        }
        else
        {
            signin_button.setBackgroundResource(R.drawable.button_unenabled);
            signin_button.setTextColor(Color.parseColor("#636e72"));
            signin_button.setEnabled(false);
        }
    }
}
