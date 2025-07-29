package com.example.appnotas.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appnotas.data.model.Nota;

import java.util.List;

@Dao
public interface NotaDao {

    @Insert
    void insertar(Nota nota);

    @Query("SELECT * FROM notas ORDER BY id DESC")
    LiveData<List<Nota>> obtenerTodas();

    @Delete
    void eliminar(Nota nota);

}