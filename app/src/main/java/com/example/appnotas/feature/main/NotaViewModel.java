package com.example.appnotas.feature.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.appnotas.data.model.Nota;
import com.example.appnotas.repository.NotaRepository;

import java.util.List;

public class NotaViewModel extends AndroidViewModel {

    private NotaRepository repository;
    private LiveData<List<Nota>> todasLasNotas;

    public NotaViewModel(@NonNull Application application) {
        super(application);
        repository = new NotaRepository(application);
        todasLasNotas = repository.obtenerTodas();
    }

    public void insertar(Nota nota) {
        repository.insertar(nota);
    }

    public void eliminar(Nota nota) {
        repository.eliminar(nota);
    }

    public LiveData<List<Nota>> obtenerTodas() {
        return todasLasNotas;
    }
}
