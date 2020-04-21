package com.gistMED.gistmd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gistMED.gistmd.Classes.PickUserAdapter;
import com.gistMED.gistmd.Classes.StaticObjects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseUserActivity extends AppCompatActivity {

    private RecyclerView usersRV;

    private ImageView menue;
    private FrameLayout frame_layout_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        menue = toolbar.findViewById(R.id.menue);
        frame_layout_icon = toolbar.findViewById(R.id.frame_layout_icon);

        menue.setVisibility(View.GONE);
        frame_layout_icon.setVisibility(View.GONE);

        usersRV = findViewById(R.id.recycleview_users);

        PickUserAdapter cardsAdapter = new PickUserAdapter(getApplicationContext(),StaticObjects.users,this);
        usersRV.setLayoutManager(new GridLayoutManager(this, 3));
        usersRV.setAdapter(cardsAdapter);

    }
}
