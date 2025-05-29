package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Clases.CarritoItem;
import com.example.sparkv_v1.R;

import java.util.ArrayList;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    private final ArrayList<CarritoItem> carritoItems;
    private final OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDelete(String servicioId);
    }

    public CarritoAdapter(ArrayList<CarritoItem> carritoItems, OnDeleteClickListener onDeleteClickListener) {
        this.carritoItems = carritoItems;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarritoItem item = carritoItems.get(position);

        holder.nombre.setText(item.getNombre());
        holder.precio.setText("€" + item.getPrecio());
        holder.cantidad.setText("Cantidad: " + item.getCantidad());
        holder.fecha.setText("Fecha: " + item.getFecha());
        holder.hora.setText("Hora: " + item.getHora());

        // Configurar botón de eliminación
        holder.eliminarBtn.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDelete(item.getServicioId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return carritoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, cantidad, fecha, hora, eliminarBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreTxt);
            precio = itemView.findViewById(R.id.precioTxt);
            cantidad = itemView.findViewById(R.id.cantidadTxt);
            fecha = itemView.findViewById(R.id.fechaTxt);
            hora = itemView.findViewById(R.id.horaTxt);
            eliminarBtn = itemView.findViewById(R.id.eliminarBtn);
        }
    }
}