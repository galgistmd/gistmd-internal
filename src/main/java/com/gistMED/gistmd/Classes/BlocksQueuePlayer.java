package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;

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

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.listener.OnVideoCompletedListener;

public class BlocksQueuePlayer

{
    private Queue<StorageReference> PathsForBlocks;
    private Queue<File> BlocksToPlay;
    private FullscreenVideoView fullscreenVideoView;
    private File IntroFile;
    private static Boolean DO = false;
    MediaController MediaController;
    private ProgressDialog BufferDialog;
    private Boolean WaitingForBlockToDownload = false;

    public BlocksQueuePlayer(ArrayList<StorageReference> pathsForBlocks, FullscreenVideoView videoView, Activity activity,File introFile) {
        PathsForBlocks = new LinkedList<>(pathsForBlocks);
        BlocksToPlay = new LinkedList<>();
        MediaController = new MediaController(activity);
        IntroFile = introFile;
        fullscreenVideoView = videoView;
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
        fullscreenVideoView.addOnVideoCompletedListener(new OnVideoCompletedListener() {
            @Override
            public void onFinished() {
                fullscreenVideoView.pause();

                PlayNextBlock();
            }
        });
    }

    private void PlayNextBlock()
    {
        if (!BlocksToPlay.isEmpty()) {
            fullscreenVideoView.changeUrl(BlocksToPlay.poll().getPath());
            fullscreenVideoView.enableAutoStart();
            LoadBlockFromStorageToQueue();
        } else {
            BufferDialog.show();
            WaitingForBlockToDownload = true;
            Log.e("Error", "Downloading block... Please wait!");
        }
    }

    private void PlayLocalIntro()
    {
        fullscreenVideoView.videoFile(IntroFile);
        fullscreenVideoView.enableAutoStart();
        LoadBlockFromStorageToQueue();
    }
}