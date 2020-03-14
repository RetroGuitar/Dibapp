package com.example.dibapp.module;

public class Comment {
    public String id;
    public float rating;
    public String description;
    public boolean privatecomment;

    public Comment (String id, float rating, String description, boolean privatecomment){
        this.id=id;
        this.rating=rating;
        this.description=description;
        this.privatecomment=privatecomment;
    }

}
