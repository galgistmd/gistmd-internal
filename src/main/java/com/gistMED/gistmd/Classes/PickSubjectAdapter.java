package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gistMED.gistmd.PersonalizerActivity;
import com.gistMED.gistmd.PickSubjectActivity;
import com.gistMED.gistmd.R;
import com.gistMED.gistmd.SplashScreenActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PickSubjectAdapter  extends RecyclerView.Adapter<PickSubjectAdapter.ViewHolder> {

    private Context mContext;
    private Activity activity;
    private ArrayList<Subject> mData;

    public PickSubjectAdapter(Context mContext, ArrayList<Subject> mData,Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_item_answer,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Subject subject = mData.get(position);

        holder.tv_subject_title.setText(subject.getLabel());

        if(subject.getIconURL()!=null) {
            Glide.with(mContext)
                    .load(subject.getIconURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_subject_icon);
        }
        else
        {
            String childRef = "Icons" + "/" + subject.getIconID() + ".png";
            StorageReference ref = StaticObjects.mStorageRef.child(childRef);
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    subject.setIconURL(uri.toString());
                    Glide.with(mContext)
                            .load(subject.getIconURL())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.img_subject_icon);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject_title;
        ImageView img_subject_icon;
        ImageView img_background;

        public ViewHolder(final View itemView) {
            super(itemView);

            tv_subject_title = itemView.findViewById(R.id.text_card);
            img_subject_icon = itemView.findViewById(R.id.image_card);
            img_background = itemView.findViewById(R.id.card_background);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subject selectedSubject = mData.get(getAdapterPosition());
                    StaticObjects.selectedSubject = selectedSubject;
                    Intent intent = new Intent(activity,PersonalizerActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }
    }

}
