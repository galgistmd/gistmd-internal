package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gistMED.gistmd.ChooseUserActivity;
import com.gistMED.gistmd.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PickUserAdapter  extends RecyclerView.Adapter<PickUserAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<User> mData;
    private Activity activity;

    public PickUserAdapter(Context mContext, ArrayList<User> mData,Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.present_user_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if(position==0)
        {
            holder.tv_subject_title.setText("משתמש חדש");
        }
        else {
            final User userToShow = mData.get(position-1);

            holder.tv_subject_title.setText(userToShow.getFirst_name() + " " + userToShow.getLast_name());

            if(userToShow.getProfileImgURL()!=null)
            {
                Glide.with(mContext)
                        .load(userToShow.getProfileImgURL()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.profile_image);
            }

            else {
                String childRef = "Profiles" + "/" + userToShow.getProfile_img() + ".png";
                StorageReference ref = StaticObjects.mStorageRef.child(childRef);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userToShow.setProfileImgURL(uri.toString());
                        Glide.with(mContext)
                                .load(userToShow.getProfileImgURL()).dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.profile_image);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject_title;
        CircleImageView profile_image;

        public ViewHolder(final View itemView) {
            super(itemView);

                tv_subject_title = itemView.findViewById(R.id.tv_user_name);
                profile_image = itemView.findViewById(R.id.profile_image);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getAdapterPosition()!=0) {
                            int pos = getAdapterPosition() - 1;
                            CustomDialogSignIn cdd = new CustomDialogSignIn(activity, mData.get(pos), mContext);
                            cdd.show();
                        }
                        else
                        {
                            CustomDialogSignUp dialog = new CustomDialogSignUp(activity,mContext);
                            dialog.show();
                        }
                    }
                });
        }
    }

}
