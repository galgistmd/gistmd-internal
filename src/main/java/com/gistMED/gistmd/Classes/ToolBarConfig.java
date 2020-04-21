package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gistMED.gistmd.R;
import com.gistMED.gistmd.UserPageActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToolBarConfig {

    public static void ConfigToolBar(final Activity activity)
    {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        CircleImageView profileImage = toolbar.findViewById(R.id.profile_image);
        try {
            Glide.with(activity.getApplicationContext())
                    .load(StaticObjects.mConnecteduserinfo.getProfileImgURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImage);
        }
        catch (Exception e)
        {

        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, UserPageActivity.class));
                activity.finish();
            }
        });
    }

}
