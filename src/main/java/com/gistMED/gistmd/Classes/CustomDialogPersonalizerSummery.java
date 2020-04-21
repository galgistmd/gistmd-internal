package com.gistMED.gistmd.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.gistMED.gistmd.CinemaActivity;
import com.gistMED.gistmd.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CustomDialogPersonalizerSummery extends Dialog {

    private Activity a;
    private ListView sum_listview;
    private List<String> answers;
    private Button gistit_button;

    public CustomDialogPersonalizerSummery(Activity a, String[] answers) {
        super(a);
        this.a = a;
        this.answers = Arrays.asList(answers);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalizer_summery_dialog);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sum_listview = findViewById(R.id.sum_listview);
        gistit_button = findViewById(R.id.gistit_button);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(a,android.R.layout.simple_list_item_1, this.answers);

        sum_listview.setAdapter(itemsAdapter);

        final CustomDialogPersonalizerSummery customDialogPersonalizerSummery = this;

        gistit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.startActivity(new Intent(a,CinemaActivity.class));
                customDialogPersonalizerSummery.dismiss();
                a.finish();
            }
        });

    }
}
