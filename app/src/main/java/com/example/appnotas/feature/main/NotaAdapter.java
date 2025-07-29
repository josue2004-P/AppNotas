package com.example.appnotas.feature.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnotas.R;
import com.example.appnotas.data.model.Nota;

import java.util.ArrayList;
import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {
    private List<Nota> listaNotas = new ArrayList<>();

    public void setNotas(List<Nota> notas) {
        this.listaNotas = notas;
        notifyDataSetChanged();
    }

    public Nota getNotaEnPosicion(int posicion) {
        return listaNotas.get(posicion);
    }

    public void eliminarNota(int posicion) {
        listaNotas.remove(posicion);
        notifyItemRemoved(posicion);
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);
        holder.bind(nota);
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, contenido, fecha;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textTitulo);
            contenido = itemView.findViewById(R.id.textContenido);
            fecha = itemView.findViewById(R.id.textFecha);
        }

        public void bind(Nota nota) {
            titulo.setText(nota.getTitulo());
            contenido.setText(nota.getContenido());
            fecha.setText(nota.getFecha());

            itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "ID: " + nota.getId(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
