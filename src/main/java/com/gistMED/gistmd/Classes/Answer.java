package com.gistMED.gistmd.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Answer {
    private String label; //the answer
    private String ID; //answer id (answer_1) for example
    private String iconID; //icon id for getting the icon from storage
    private String iconURL; //the icon url from storage for glide
    private boolean selected = false; //is the answer selected flag

    public Answer(String label, String iconID, String ID) {
        this.label = label;
        this.iconID = iconID;
        this.ID = ID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setIconURL(String iconURL)
    {
        this.iconURL = iconURL;
    }

    public String getURL()
    {
        return this.iconURL;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLabel() {
        return this.label;
    }

    public String getID() {
        return ID;
    }

    public String getIconID() {
        return iconID;
    }


}