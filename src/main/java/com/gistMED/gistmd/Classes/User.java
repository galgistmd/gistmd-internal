package com.gistMED.gistmd.Classes;

public class User {

    private String first_name;
    private String last_name;
    private String userID;
    private String managing_organization;
    private String profileImgURL;
    private String profile_img;
    private String user_gender;
    private String user_role;
    private String user_lang;
    private String user_email;
    private String token;

    public User(String first_name, String last_name, String managing_organization, String profile_img, String user_gender, String user_lang, String user_role, String user_email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.managing_organization = managing_organization;
        this.profile_img = profile_img;
        this.user_gender = user_gender;
        this.user_role = user_role;
        this.user_lang = user_lang;
        this.user_email = user_email;
    }

    public User()
    {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getManaging_organization() {
        return managing_organization;
    }

    public void setManaging_organization(String managing_organization) {
        this.managing_organization = managing_organization;
    }

    public String getProfileImgURL() {
        return profileImgURL;
    }

    public void setProfileImgURL(String profileImgURL) {
        this.profileImgURL = profileImgURL;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_lang() {
        return user_lang;
    }

    public void setUser_lang(String user_lang) {
        this.user_lang = user_lang;
    }

    public enum Role
    {
        Nurse,Doctor;
    }

    public enum Gender
    {
        Female,Male;
    }

}
