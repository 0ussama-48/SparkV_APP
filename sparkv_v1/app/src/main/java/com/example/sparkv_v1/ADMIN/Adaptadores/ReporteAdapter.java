package com.example.sparkv_v1.ADMIN.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Clases.Reporte;
import com.example.sparkv_v1.R;

import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ViewHolder> {
    private final List<Reporte> reportes;
    private final OnEditClickListener onEditClickListener;

    public interface OnEditClickListener {
        void onEditClick(Reporte reporte);
    }

    public ReporteAdapter(List<Reporte> reportes, OnEditClickListener onEditClickListener) {
        this.reportes = reportes;
        this.onEditClickListener = onEditClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_adm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reporte reporte = reportes.get(position);
        holder.descripcion.setText("Descripción: " + reporte.getDescripcion());
        holder.username.setText("Usuario: " + reporte.getUserName());
        holder.fecha.setText("Fecha: " + reporte.getFecha());

        holder.itemView.setOnClickListener(v -> onEditClickListener.onEditClick(reporte));
    }

    @Override
    public int getItemCount() {
        return reportes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion, username, fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            username = itemView.findViewById(R.id.tvUsername);
            fecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}