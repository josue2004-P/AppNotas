package com.example.appnotas.feature.add;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.appnotas.R;
import com.example.appnotas.data.model.Nota;
import com.example.appnotas.feature.main.NotaViewModel;
import com.example.appnotas.feature.notificaciones.RecordatorioReceiver;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitulo, editContenido;

    private TextView textFecha;


    private NotaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTitulo = findViewById(R.id.editTitulo);
        editContenido = findViewById(R.id.editContenido);
        textFecha = findViewById(R.id.textFecha);
        viewModel = new ViewModelProvider(this).get(NotaViewModel.class);

        // Permiso de notificación para Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        textFecha.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {

                        // Después de seleccionar la fecha, abre el TimePicker
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                this,
                                (timeView, hourOfDay, minute) -> {
                                    // Combina fecha y hora
                                    Calendar fechaSeleccionada = Calendar.getInstance();
                                    fechaSeleccionada.set(Calendar.YEAR, year);
                                    fechaSeleccionada.set(Calendar.MONTH, month);
                                    fechaSeleccionada.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    fechaSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    fechaSeleccionada.set(Calendar.MINUTE, minute);
                                    fechaSeleccionada.set(Calendar.SECOND, 0);

                                    // Mostrar texto combinado
                                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                    textFecha.setText(formato.format(fechaSeleccionada.getTime()));

                                    // Guardar fecha exacta en milisegundos como tag
                                    textFecha.setTag(fechaSeleccionada.getTimeInMillis());
                                },
                                calendario.get(Calendar.HOUR_OF_DAY),
                                calendario.get(Calendar.MINUTE),
                                true
                        );
                        timePickerDialog.show();
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });


        findViewById(R.id.btnGuardar).setOnClickListener(v -> {

            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();
            Object tagFecha = textFecha.getTag();  // ← Validar si ya hay una fecha

            if (titulo.isEmpty() || contenido.isEmpty() || tagFecha == null) {
                Toast.makeText(this, "Llena todos los campos y selecciona una fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            long fechaMillis = (long) tagFecha;

            // 1. Guardar la nota
            Nota nota = new Nota();
            nota.setTitulo(titulo);
            nota.setContenido(contenido);
            nota.setFecha(textFecha.getText().toString()); // fecha en formato dd/MM/yyyy HH:mm
            viewModel.insertar(nota);

            //PROGRAMAR ALERTA
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 15);

            Intent intent = new Intent(this, RecordatorioReceiver.class);
            intent.putExtra("titulo", "🔔 Alerta programada por el usuario");
            intent.putExtra("fechaMillis", fechaMillis); // ← Aquí se manda la fecha real

            PendingIntent pi = PendingIntent.getBroadcast(this, 999, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (am.canScheduleExactAlarms()) {
                    am.setExact(AlarmManager.RTC_WAKEUP, fechaMillis, pi); // ← Se usa la fecha del input
                    Toast.makeText(this, "✅ Notificación programada con fecha seleccionada", Toast.LENGTH_SHORT).show();
                } else {
                    Intent permisoIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(permisoIntent);
                    Toast.makeText(this, "⚠️ Habilita alarmas exactas en Configuración", Toast.LENGTH_LONG).show();
                }
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, fechaMillis, pi); // ← También aquí
                Toast.makeText(this, "✅ Notificación programada con fecha seleccionada", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
    }
}
