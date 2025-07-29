package com.example.appnotas.feature.notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.appnotas.R;
import com.example.appnotas.feature.main.MainActivity;

public class RecordatorioReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("titulo");
        long fechaProgramada = intent.getLongExtra("fechaMillis", -1);
        long ahora = System.currentTimeMillis();

        Log.d("NOTIF_DEBUG", "🔥 onReceive ejecutado");

        if (fechaProgramada == -1) {
            Log.d("NOTA", "❌ fechaMillis no recibido, cancelando...");
            return;
        }

        if (Math.abs(ahora - fechaProgramada) > 60_000) {
            Log.d("NOTA", "⏱️ No coincide con hora programada, ignorando...");
            return;
        }

        Toast.makeText(context, "🔔 Recordatorio: " + titulo, Toast.LENGTH_LONG).show();
        Log.d("NOTA", "✅ Mostrando notificación: " + titulo);

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    "canal_notas", "Recordatorios", NotificationManager.IMPORTANCE_HIGH);
            canal.setDescription("Canal para recordatorios de notas");
            canal.enableLights(true);
            canal.setLightColor(Color.BLUE);
            manager.createNotificationChannel(canal);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "canal_notas")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("📌 Nota programada")
                .setContentText(titulo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
