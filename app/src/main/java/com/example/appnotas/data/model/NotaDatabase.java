package com.example.appnotas.data.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Nota.class}, version = 1, exportSchema = false)
public abstract class NotaDatabase extends RoomDatabase {

    private static NotaDatabase instancia;

    public abstract NotaDao notaDao();

    public static synchronized NotaDatabase obtenerInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                            NotaDatabase.class, "notas_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instancia;
    }
}