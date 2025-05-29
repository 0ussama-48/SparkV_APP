package com.example.sparkv_v1.ADMIN.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Clases.Servicio;
import com.example.sparkv_v1.R;

import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private final List<Servicio> listaServicios;
    private final OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEditClick(Servicio servicio);
    }

    public ServicioAdapter(List<Servicio> listaServicios, OnEditClickListener listener) {
        this.listaServicios = listaServicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = listaServicios.get(position);
        holder.textViewNombre.setText(servicio.getNombre());
        holder.textViewDescripcion.setText(servicio.getDescripcion());
        holder.textViewPrecio.setText("Precio: $" + servicio.getPrecio());

        holder.btnEditar.setOnClickListener(v -> {
            listener.onEditClick(servicio);
        });
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewDescripcion, textViewPrecio;
        Button btnEditar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}