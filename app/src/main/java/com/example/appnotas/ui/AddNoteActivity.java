package com.example.appnotas.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.appnotas.R;
import com.example.appnotas.data.model.Nota;
import com.example.appnotas.viewmodel.NotaViewModel;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitulo, editContenido;
    private NotaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTitulo = findViewById(R.id.editTitulo);
        editContenido = findViewById(R.id.editContenido);
        viewModel = new ViewModelProvider(this).get(NotaViewModel.class);

        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();

            if (titulo.isEmpty() || contenido.isEmpty()) {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Nota nota = new Nota();
            nota.setTitulo(titulo);
            nota.setContenido(contenido);
            viewModel.insertar(nota);
            finish();
        });

        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
    }
}
