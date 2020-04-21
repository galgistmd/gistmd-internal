package com.gistMED.gistmd.Classes;

public class Comment {

    private String comment_uid;
    private String content;
    private String poster_id;
    //private int timestamp;

    public Comment(String comment_uid, String content, String poster_id, int timestamp) {
        this.comment_uid = comment_uid;
        this.content = content;
        this.poster_id = poster_id;
   //     this.timestamp = timestamp;
    }
}
