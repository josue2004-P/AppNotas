package com.example.appnotas.feature.main;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnotas.R;
import com.example.appnotas.data.model.Nota;
import com.example.appnotas.data.weather.WeatherResponse;
import com.example.appnotas.feature.add.AddNoteActivity;
import com.example.appnotas.repository.WeatherRepository;

public class MainActivity extends AppCompatActivity {

    private NotaViewModel viewModel;
    private NotaAdapter adapter;
    private TextView textClima;
    private static final String API_KEY = "c34d77e6aa24638f3b5cf283f7435da1";
    private static final String CITY = "Veracruz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textClima = findViewById(R.id.textClima);

        //API CLIMA
        WeatherRepository repository = new WeatherRepository();
        repository.getWeather(CITY, API_KEY, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse response) {
                String description = response.weather.get(0).description;
                float temperature = response.main.temp;

                String clima = "Ciudad: " + CITY + "\nClima: " + description + "\nTemperatura: " + temperature + "°C";

                // Mostrar en la UI (asegúrate de hacerlo en el hilo principal)
                runOnUiThread(() -> textClima.setText(clima));
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> textClima.setText("Error al obtener el clima"));
                Log.e("Clima", "Error al obtener clima: " + t.getMessage());
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerNotas);
        adapter = new NotaAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NotaViewModel.class);
        viewModel.obtenerTodas().observe(this, notas -> adapter.setNotas(notas));

        findViewById(R.id.fabAgregarNota).setOnClickListener(v -> {
            startActivity(new Intent(this, AddNoteActivity.class));
        });
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Nota nota = adapter.getNotaEnPosicion(position);

                viewModel.eliminarNota(nota);  // Elimina de la base de datos
                adapter.eliminarNota(position); // Elimina visualmente
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#f44336")); // Rojo

                // Fondo rojo
                RectF background = new RectF(
                        itemView.getRight() + dX,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom()
                );
                c.drawRect(background, paint);

                // Ícono de papelera
                Drawable icon = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_basura);
                if (icon != null) {
                    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }
}