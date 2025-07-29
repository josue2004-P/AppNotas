package com.example.appnotas.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.appnotas.data.model.Nota;
import com.example.appnotas.data.db.NotaDao;
import com.example.appnotas.data.db.NotaDatabase;

import java.util.List;

public class NotaRepository {

    private NotaDao notaDao;
    private LiveData<List<Nota>> todasLasNotas;

    public NotaRepository(Application application) {
        NotaDatabase db = NotaDatabase.obtenerInstancia(application);
        notaDao = db.notaDao();
        todasLasNotas = notaDao.obtenerTodas();
    }

    public void insertar(Nota nota) {
        new InsertarNotaAsync(notaDao).execute(nota);
    }

    public void eliminar(Nota nota) {
        new EliminarNotaAsync(notaDao).execute(nota);
    }

    public LiveData<List<Nota>> obtenerTodas() {
        return todasLasNotas;
    }

    private static class InsertarNotaAsync extends AsyncTask<Nota, Void, Void> {
        private NotaDao dao;
        InsertarNotaAsync(NotaDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Nota... notas) {
            dao.insertar(notas[0]);
            return null;
        }
    }

    private static class EliminarNotaAsync extends AsyncTask<Nota, Void, Void> {
        private NotaDao dao;
        EliminarNotaAsync(NotaDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Nota... notas) {
            dao.eliminar(notas[0]);
            return null;
        }
    }
}