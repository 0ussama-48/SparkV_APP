package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Clases.ReservaDomain;
import com.example.sparkv_v1.R;
import java.util.ArrayList;

public class ReservasAdaptador extends RecyclerView.Adapter<ReservasAdaptador.ViewHolder> {
    private final ArrayList<ReservaDomain> listaReservas;

    public ReservasAdaptador(ArrayList<ReservaDomain> listaReservas) {
        this.listaReservas = listaReservas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservaDomain item = listaReservas.get(position);
        holder.servicio.setText(item.getServicio());
        holder.fecha.setText("Fecha: " + item.getFecha());
        holder.hora.setText("Hora: " + item.getHora());
        holder.estado.setText("Estado: " + item.getEstado());
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView servicio, fecha, hora, estado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            servicio = itemView.findViewById(R.id.txtServicio);
            fecha = itemView.findViewById(R.id.txtFecha);
            hora = itemView.findViewById(R.id.txtHora);
            estado = itemView.findViewById(R.id.txtEstado);
        }
    }
}