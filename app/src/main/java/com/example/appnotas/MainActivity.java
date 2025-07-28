package com.example.appnotas;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnotas.adapter.NotaAdapter;
import com.example.appnotas.ui.AddNoteActivity;
import com.example.appnotas.viewmodel.NotaViewModel;

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