package com.example.dibapp.module;

public class Course {

    public String nome;
    public String descrizione;
    public String professore;
    public String laurea;
    public String chiave;

    public Course(String nome, String descrizione, String professore,
                  String laurea, String chiave) {

        this.descrizione=descrizione;
        this.nome=nome;
        this.professore=professore;
        this.laurea=laurea;
        this.chiave=chiave;

    }
}
