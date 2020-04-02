package com.gistMED.gistmd.Classes;

public class Answer {
    private String label;
    private String ID;
    private String iconID;

    public Answer(String label, String iconID,String ID) {
        this.label = label;
        this.iconID = iconID;
        this.ID = ID;
    }

    public String getLabel()
    {
        return this.label;
    }

    public String getID() {
        return ID;
    }

    public String getIconID() {
        return iconID;
    }
}
