package com.example.dibapp.module;

public class User {
    public String name;
    public String matricola;
    public Boolean teacher;
    public String id;

    public User(){

    }

    public User(String id, String name, String matricola, Boolean teacher){
        this.id = id;
        this.matricola=matricola;
        this.name=name;
        this.teacher=teacher;
    }

    public String getName() {
        return name;
    }

    public String getMatricola() {
        return matricola;
    }

    public Boolean getTeacher() {
        return teacher;
    }
}
