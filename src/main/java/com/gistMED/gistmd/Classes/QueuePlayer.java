package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.gistMED.gistmd.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QueuePlayer {

    private ArrayList<StorageReference> PathsForBlocks;
    private ArrayList<File> BlocksToPlay;
    private static int CurrentDownloadingBlockIndex = 0;
    private static int CurrentPlayingBlockIndex = 0;
    private static Boolean VideoIsPlaying = false;
    ProgressDialog BufferDialog;
    private VideoView videoView;



    public QueuePlayer(ArrayList<StorageReference> pathsForBlocks, VideoView videoView,Activity activity) {
        PathsForBlocks = pathsForBlocks;
        this.videoView = videoView;
        BlocksToPlay = new ArrayList<>();
        MediaController m = new MediaController(activity);
        videoView.setMediaController(m);
        m.setAnchorView(videoView);
        m.setVisibility(View.GONE);
        performActionAsync();
        BufferDialog = new ProgressDialog(activity);
    }


    public void performActionAsync(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                PrepareFlow();
            }
        }).start();
    }


    private void PrepareFlow()
    {
        File localFile = null;
        try {
            localFile = File.createTempFile("videos", "mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;
        PathsForBlocks.get(CurrentDownloadingBlockIndex).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(CurrentDownloadingBlockIndex == (PathsForBlocks.size()-1)){
                        BlocksToPlay.add(finalLocalFile);
                        //stop
                    }
                    else {
                        BlocksToPlay.add(finalLocalFile);
                        CurrentDownloadingBlockIndex++;
                        if(!VideoIsPlaying) {
                            playVideo();
                            BufferDialog.dismiss();
                        }
                        PrepareFlow();
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    public void Start()
    {
        PlayLocalIntro();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                playVideo();
            }
        });

    }

    public void playVideo() {
        if(!(BlocksToPlay.size() <= CurrentPlayingBlockIndex)) {
            videoView.setVideoPath(BlocksToPlay.get(CurrentPlayingBlockIndex).getPath());
            videoView.start();
            VideoIsPlaying = true;
            this.CurrentPlayingBlockIndex++;
            Log.d("queue",BlocksToPlay.toString());
        }
        else
        {
            BufferDialog.show();
            Log.d("downloading","video number" + CurrentPlayingBlockIndex);
        }
    }

    public void AddFileToBlocksToPlay(File file)
    {
        this.BlocksToPlay.add(file);
    }

    private void PlayLocalIntro()
    {
        Uri video = Uri.parse("android.resource://" + "com.gistMED.gistmd" + "/"
                + R.raw.gist_logo_animation); //do not add any extension
        videoView.setVideoURI(video);
        videoView.start();
    }


}
