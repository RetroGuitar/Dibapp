package com.example.dibapp.module;

public class Comment {
    public String id;
    public int rating;
    public String descrption;
    public boolean privatecomment;

    public Comment (String id, int rating, String descrption, boolean privatecomment){
        this.id=id;
        this.rating=rating;
        this.descrption=descrption;
        this.privatecomment=privatecomment;
    }
    
}
