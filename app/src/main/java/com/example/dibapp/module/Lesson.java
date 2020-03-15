package com.example.dibapp.module;

public class Lesson {

    public String id;
    public String descrizione;
    public String data;
    public String ora_i;
    public String ora_f;

    public boolean iniziata;

    public Lesson(String id, String descrizione, String data, String ora_i,
                  String ora_f, boolean iniziata) {

        this.id = id;
        this.descrizione=descrizione;
        this.data=data;
        this.ora_i=ora_i;
        this.ora_f=ora_f;

        this.iniziata=iniziata;




        }

    public Lesson (String id, String descrizione){
        this.id=id;
        this.descrizione=descrizione;
    }
}
