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
        this.pathsForBlocks = new LinkedList<>(pathsForBlocks); //all the paths for blocks from storage
        blocksToPlay = new LinkedList<>(); //files to play from queue
       // mediaController = new MediaController(activity);
        this.introFile = introFile;
        fullScreenVideoView = videoView;
        bufferDialog = new ProgressDialog(activity); //buffering video dialog
        // MediaController.setVisibility(View.GONE);
    }

    private void LoadBlockFromStorageToQueue()
    {
        if (!pathsForBlocks.isEmpty()) { //if no more paths for blocks to play stop
            File localFile = null;
            try {
                localFile = File.createTempFile("videos", "mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }

            final File finalLocalFile = localFile;
            pathsForBlocks.poll().getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) { //get file by path from storage
                    blocksToPlay.add(finalLocalFile);
                    if(waitingForBlockToDownload) //if waited to block to download than play it
                    {
                        bufferDialog.dismiss(); // cancel buffering dialog
                        PlayNextBlock(); //play the block
                        waitingForBlockToDownload = false; //set flag to false because not waiting for block
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
        PlayLocalIntro(); //play the local intro
        fullScreenVideoView.addOnVideoCompletedListener(new OnVideoCompletedListener() {
            @Override
            public void onFinished() {
                PlayNextBlock();
            }
        });//when block played start next one
    }

    private void PlayNextBlock()
    {
        if (!blocksToPlay.isEmpty()) { //if no files to play wait for block to download
            fullScreenVideoView.changeVideoFile(blocksToPlay.poll());
            fullScreenVideoView.enableAutoStart();
            LoadBlockFromStorageToQueue();
        } else { //wait for block
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