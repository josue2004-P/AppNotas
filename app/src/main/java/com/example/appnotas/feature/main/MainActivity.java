package com.example.appnotas.feature.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnotas.R;
import com.example.appnotas.feature.add.AddNoteActivity;

public class MainActivity extends AppCompatActivity {

    private NotaViewModel viewModel;
    private NotaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerNotas);
        adapter = new NotaAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NotaViewModel.class);
        viewModel.obtenerTodas().observe(this, notas -> adapter.setNotas(notas));

        findViewById(R.id.fabAgregarNota).setOnClickListener(v -> {
            startActivity(new Intent(this, AddNoteActivity.class));
        });
    }
}