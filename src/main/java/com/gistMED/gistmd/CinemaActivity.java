package com.gistMED.gistmd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.gistMED.gistmd.Classes.BlocksQueuePlayer;
import com.gistMED.gistmd.Classes.QueuePlayer;
import com.gistMED.gistmd.Classes.StaticObjects;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;


public class CinemaActivity extends AppCompatActivity {

    private FullscreenVideoView videoView;
    private StorageReference mStorageRef;
    private Button btn_scale_video_view;
    private QueuePlayer queuePlayer;
    private ArrayList<StorageReference> pathsForBlocks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema);

        videoView = findViewById(R.id.video_view);
        mStorageRef = FirebaseStorage.getInstance().getReference();

       if(StaticObjects.mBlocksFromFlow !=null)
         pathsForBlocks = CreatePathsForBlocks(StaticObjects.mBlocksFromFlow);

       try{
           InputStream inputStream = getResources().openRawResource(R.raw.gist_logo_animation);
           File tempFile = File.createTempFile("pre", "suf");
           copyFile(inputStream, new FileOutputStream(tempFile));
           BlocksQueuePlayer BlocksQueuePlayer = new BlocksQueuePlayer(pathsForBlocks,videoView,this,tempFile);
           BlocksQueuePlayer.Start();
       } catch (IOException e) {
           throw new RuntimeException("Can't create temp file ", e);
       }

   }


   private void copyFile(InputStream in, OutputStream out) throws IOException {
       byte[] buffer = new byte[1024];
       int read;
       while((read = in.read(buffer)) != -1){
           out.write(buffer, 0, read);
       }
   }

   private ArrayList<StorageReference> CreatePathsForBlocks(LinkedHashMap<String,String> BlocksFromFlow)
   {
       ArrayList<StorageReference> PathsForBlocks = new ArrayList<>();

       for (Map.Entry<String, String> entry : BlocksFromFlow.entrySet()) {
           String v = entry.getValue();
           String childRef = getString(R.string.videoFolderName)+"/"+"Nikur"+"/"+v+"."+getString(R.string.videoFormat);
           StorageReference ref = mStorageRef.child(childRef);
           PathsForBlocks.add(ref);
       }
       return PathsForBlocks;
    }
}