package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sparkv_v1.R;
import com.example.sparkv_v1.CLIENTE.Clases.Direccion;

import java.util.List;

public class DireccionesAdaptador extends RecyclerView.Adapter<DireccionesAdaptador.ViewHolder> {
    private List<Direccion> direccionesList;
    private Context context;

    public DireccionesAdaptador(List<Direccion> direccionesList, Context context) {
        this.direccionesList = direccionesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_direccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Direccion direccion = direccionesList.get(position);

        // Mostrar datos de la dirección
        holder.calle.setText("Calle: " + direccion.getCalle());
        holder.numeroCasa.setText("Número: " + direccion.getNumeroCasa());
        holder.ciudad.setText("Ciudad: " + direccion.getCiudad());
        holder.codigoPostal.setText("Código Postal: " + direccion.getCodigoPostal());

        // Configurar OnClickListener para seleccionar dirección
        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_calle", direccion.getCalle());
            resultIntent.putExtra("selected_numero", direccion.getNumeroCasa());
            resultIntent.putExtra("selected_ciudad", direccion.getCiudad());
            resultIntent.putExtra("selected_codigo_postal", direccion.getCodigoPostal());
            ((android.app.Activity) context).setResult(android.app.Activity.RESULT_OK, resultIntent);
            ((android.app.Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return direccionesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView calle, numeroCasa, ciudad, codigoPostal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            calle = itemView.findViewById(R.id.calle);
            numeroCasa = itemView.findViewById(R.id.numeroCasa);
            ciudad = itemView.findViewById(R.id.ciudad);
            codigoPostal = itemView.findViewById(R.id.codigoPostal);
        }
    }
}