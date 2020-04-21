package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gistMED.gistmd.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PersonalizerAdapter extends RecyclerView.Adapter<PersonalizerAdapter.MyViewHolder> {

    private Context mContext;
    private Activity a;
    private ArrayList<InformationPackage> Data;
    private int resID;
    private String viewName;
    private Boolean normalView = false;
    private String[] answers;

    public PersonalizerAdapter(Context mContext, ArrayList<InformationPackage> Data, Activity a)
    {
        this.mContext = mContext;
        this.Data = Data;
        this.viewName = Data.get(StaticObjects.pagePos).getCollectionView();
        this.resID = mContext.getResources().getIdentifier("cardview_item_answer", "layout", mContext.getPackageName());
        if(!viewName.equals("bubbles_view"))
            normalView=true;
        answers = new String[Data.size()];
        this.a = a;
    }

    public ArrayList<InformationPackage> getData()
    {
        return this.Data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(this.resID,parent,false);

        return new MyViewHolder(view, Data,resID);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,int position) {

        InformationPackage currentInfoPack = Data.get(StaticObjects.pagePos);
        final Answer answerToShow = currentInfoPack.getAnswers().get(position);

        if(normalView) {
            holder.tv_answer_title.setText(answerToShow.getLabel());

            if (answerToShow.getURL() != null) {
                Glide.with(mContext)
                        .load(answerToShow.getURL())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.img_answer_icon);
            } else {
                String childRef = "Icons" + "/" + answerToShow.getIconID() + "_inverted.png";
                StorageReference ref = StaticObjects.mStorageRef.child(childRef);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        answerToShow.setIconURL(uri.toString());
                        Glide.with(mContext)
                                .load(answerToShow.getURL())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.img_answer_icon);
                    }
                });
            }

            if (answerToShow.isSelected()) //if the answer is selected ui updated
            {
                holder.tv_answer_title.setTextColor(Color.WHITE);
                holder.img_background.setBackgroundColor(Color.parseColor("#F02A4C"));
            }
            else
            {
                holder.tv_answer_title.setTextColor(Color.parseColor("#444444"));
                holder.img_background.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Data.get(StaticObjects.pagePos).getAnswers().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_answer_title;
        ImageView img_answer_icon;
        ImageView img_background;


        public MyViewHolder(final View itemView, final ArrayList<InformationPackage> mData,int resID) {
            super(itemView);

            if(normalView) {
                tv_answer_title = itemView.findViewById(R.id.text_card);
                img_answer_icon = itemView.findViewById(R.id.image_card);
                img_background = itemView.findViewById(R.id.card_background);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InformationPackage currentInfoPack = mData.get(StaticObjects.pagePos);
                        String questionPageId = currentInfoPack.getID();
                        Answer touchedAnswer = currentInfoPack.getAnswers().get(getAdapterPosition());
                        mData.get(StaticObjects.pagePos).setAnswerSelected(touchedAnswer);
                        if(StaticObjects.set.containsKey(questionPageId))
                            StaticObjects.set.replace(questionPageId, touchedAnswer.getID()); //replace the selected answer at this page
                        else
                            StaticObjects.set.put(questionPageId,touchedAnswer.getID());
                        answers[StaticObjects.pagePos] = touchedAnswer.getLabel();
                        notifyDataSetChanged();
                        Log.e("hashmap",StaticObjects.set.toString());

                        if(StaticObjects.set.size()==mData.size()+1) //that means selected everything so create vid
                        {
                            //start dialog
                            CustomDialogPersonalizerSummery customDialogPersonalizerSummery = new CustomDialogPersonalizerSummery(a,answers);
                            customDialogPersonalizerSummery.show();
                        }
                    }
                });
            }
        }
    }
}
