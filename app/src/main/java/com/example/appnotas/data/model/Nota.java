package com.example.appnotas.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notas")
public class Nota {
    @PrimaryKey(autoGenerate = true)
    private  int id;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "contenido")
    private  String contenido;

    //GETTERS Y SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public  void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}