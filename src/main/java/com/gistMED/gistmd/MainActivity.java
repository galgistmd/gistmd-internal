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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;


public class MainActivity extends AppCompatActivity {

    private FullscreenVideoView videoView;
    private StorageReference mStorageRef;
    private Button btn_scale_video_view;
    private QueuePlayer queuePlayer;
    private ArrayList<StorageReference> PathsForBlocks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);
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

        try{
            InputStream inputStream = getResources().openRawResource(R.raw.gist_logo_animation);
            File tempFile = File.createTempFile("pre", "suf");
            copyFile(inputStream, new FileOutputStream(tempFile));
            BlocksQueuePlayer BlocksQueuePlayer = new BlocksQueuePlayer(PathsForBlocks,videoView,this,tempFile);
            BlocksQueuePlayer.Start();
            // Now some_file is tempFile .. do what you like
        } catch (IOException e) {
            throw new RuntimeException("Can't create temp file ", e);
        }

       // String path = "android.resource://" + getPackageName() + "/" + R.raw.gist_logo_animation;
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}