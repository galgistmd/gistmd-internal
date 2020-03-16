package com.gistMED.gistmd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gistMED.gistmd.Classes.BlocksQueuePlayer;
import com.gistMED.gistmd.Classes.QueuePlayer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private StorageReference mStorageRef;
    private Button btn_scale_video_view;
    private QueuePlayer queuePlayer;
    private ArrayList<StorageReference> PathsForBlocks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);
        btn_scale_video_view = findViewById(R.id.btn_scale_video_view);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference ref3 = mStorageRef.child("Blocks/Nikur/intro1.mp4");
        StorageReference ref4 = mStorageRef.child("Blocks/Nikur/intro2.mp4");
        StorageReference ref5 = mStorageRef.child("Blocks/Nikur/12.mp4");
        StorageReference ref6 = mStorageRef.child("Blocks/Nikur/18.mp4");


        PathsForBlocks.add(ref3);
        PathsForBlocks.add(ref4);
        PathsForBlocks.add(ref3);
        PathsForBlocks.add(ref4);
        PathsForBlocks.add(ref5);


        //queuePlayer = new QueuePlayer(PathsForBlocks,videoView,this);
        //queuePlayer.Start();
        String path = "android.resource://" + getPackageName() + "/" + R.raw.gist_logo_animation;
        BlocksQueuePlayer BlocksQueuePlayer = new BlocksQueuePlayer(PathsForBlocks,videoView,this,path);
        BlocksQueuePlayer.Start();

        btn_scale_video_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoFullScreen();
            }
        });
    }

    private void GoFullScreen()
    {

    }

}
