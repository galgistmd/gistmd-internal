package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
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
    private Queue<StorageReference> pathsForBlocks;
    private Queue<File> blocksToPlay;
    private FullscreenVideoView fullScreenVideoView;
    private File introFile;
    MediaController mediaController;
    private ProgressDialog bufferDialog;
    private Boolean waitingForBlockToDownload = false;

    public BlocksQueuePlayer(ArrayList<StorageReference> pathsForBlocks, FullscreenVideoView videoView, Activity activity,File introFile) {
        this.pathsForBlocks = new LinkedList<>(pathsForBlocks);
        blocksToPlay = new LinkedList<>();
        mediaController = new MediaController(activity);
        this.introFile = introFile;
        fullScreenVideoView = videoView;
        bufferDialog = new ProgressDialog(activity);
        // MediaController.setVisibility(View.GONE);
    }

    private void LoadBlockFromStorageToQueue()
    {
        if (!pathsForBlocks.isEmpty()) {
            File localFile = null;
            try {
                localFile = File.createTempFile("videos", "mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }

            final File finalLocalFile = localFile;
            pathsForBlocks.poll().getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    blocksToPlay.add(finalLocalFile);
                    if(waitingForBlockToDownload)
                    {
                        bufferDialog.dismiss();
                        PlayNextBlock();
                        waitingForBlockToDownload = false;
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
        fullScreenVideoView.addOnVideoCompletedListener(new OnVideoCompletedListener() {
            @Override
            public void onFinished() {
                PlayNextBlock();
            }
        });
    }

    private void PlayNextBlock()
    {
        if (!blocksToPlay.isEmpty()) {
            fullScreenVideoView.changeUrl(blocksToPlay.poll().getPath());
            fullScreenVideoView.enableAutoStart();
            LoadBlockFromStorageToQueue();
        } else {
            bufferDialog.show();
            waitingForBlockToDownload = true;
            Log.e("Error", "Downloading block... Please wait!");
        }
    }

    private void PlayLocalIntro()
    {
        fullScreenVideoView.videoFile(introFile);
        fullScreenVideoView.enableAutoStart();
        LoadBlockFromStorageToQueue();
    }
}