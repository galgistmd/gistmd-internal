package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BlocksQueuePlayer

{
    private Queue<StorageReference> PathsForBlocks;
    private Queue<File> BlocksToPlay;
    private VideoView VideoView;
    private String PathForLocalIntro;
    MediaController MediaController;
    private ProgressDialog BufferDialog;
    private Boolean WaitingForBlockToDownload = false;

    public BlocksQueuePlayer(ArrayList<StorageReference> pathsForBlocks, VideoView videoView, Activity activity,String pathForLocalIntro) {
        PathsForBlocks = new LinkedList<>(pathsForBlocks);
        BlocksToPlay = new LinkedList<>();
        MediaController = new MediaController(activity);
        PathForLocalIntro = pathForLocalIntro;
        VideoView = videoView;
        VideoView.setMediaController(MediaController);
        BufferDialog = new ProgressDialog(activity);
        // MediaController.setVisibility(View.GONE);
    }

    private void LoadBlockFromStorageToQueue()
    {
        if (!PathsForBlocks.isEmpty()) {
            File localFile = null;
            try {
                localFile = File.createTempFile("videos", "mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }

            final File finalLocalFile = localFile;
            PathsForBlocks.poll().getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    BlocksToPlay.add(finalLocalFile);
                    if(WaitingForBlockToDownload)
                    {
                        BufferDialog.dismiss();
                        PlayNextBlock();
                        WaitingForBlockToDownload = false;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else{
            Log.e("End", "No more blocks to play!");
        }
    }

    public void Start()
    {
        PlayLocalIntro();
        VideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                PlayNextBlock();
            }
        });
    }

    private void PlayNextBlock()
    {
            if (!BlocksToPlay.isEmpty()) {
                VideoView.setVideoPath(BlocksToPlay.poll().getPath());
                VideoView.start();
                LoadBlockFromStorageToQueue();
            } else {
                BufferDialog.show();
                WaitingForBlockToDownload = true;
                Log.e("Error", "Downloading block... Please wait!");
            }
    }

    private void PlayLocalIntro()
    {
        VideoView.setVideoURI(Uri.parse(PathForLocalIntro));
        VideoView.start();
        LoadBlockFromStorageToQueue();
    }


}
