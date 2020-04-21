package com.gistMED.gistmd.Classes;

public class Subject {

    private String ID;
    private String label;
    private String labelID;
    private String iconID;
    private String iconURL;
    private String viewCollectionNumber; //which view collection use

    public Subject(String ID, String labelID, String iconID, String viewCollectionNumber) {
        this.ID = ID;
        this.labelID = labelID;
        this.iconID = iconID;
        this.viewCollectionNumber = viewCollectionNumber;
    }

    public String getIconID() {
        return iconID;
    }

    public void setIconID(String iconID) {
        this.iconID = iconID;
    }


    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelID() {
        return labelID;
    }

    public void setLabelID(String labelID) {
        this.labelID = labelID;
    }

    public String getViewCollectionNumber() {
        return viewCollectionNumber;
    }

    public void setViewCollectionNumber(String viewCollectionNumber) {
        this.viewCollectionNumber = viewCollectionNumber;
    }
}
