package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.EditarPerfilActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.TerminosCondicionesActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Soporte.PoliticaPrivacidadActividad;
import com.example.sparkv_v1.CLIENTE.Clases.Opcion;
import com.example.sparkv_v1.CambiarContrasenaActivity;
import com.example.sparkv_v1.LIMPIADOR.Actividades.HistorialActivity;
import com.example.sparkv_v1.R;

import java.util.List;

public class PerfilAdaptador extends RecyclerView.Adapter<PerfilAdaptador.ViewHolder> {

    private List<Opcion> opciones;
    private Context context;

    public PerfilAdaptador(List<Opcion> opciones, Context context) {
        this.opciones = opciones;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opcion_perfil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Opcion opcion = opciones.get(position);
        holder.icon.setImageResource(opcion.getIcono());
        holder.text.setText(opcion.getTitulo());

        // Configurar OnClickListener para abrir actividades específicas
        holder.itemView.setOnClickListener(v -> {
            switch (opcion.getTitulo()) {
                case "Editar perfil":
                    context.startActivity(new Intent(context, EditarPerfilActivity.class));
                     break;
                case "Cambiar contraseña":
                    context.startActivity(new Intent(context, CambiarContrasenaActivity.class));
                    break;
                case "Historial":
                    context.startActivity(new Intent(context, HistorialActivity.class));
                    break;
                case "Política de Privacidad":
                    context.startActivity(new Intent(context, PoliticaPrivacidadActividad.class));
                    break;
                case "Términos y Condiciones":
                    context.startActivity(new Intent(context, TerminosCondicionesActivity.class));
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return opciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            text = itemView.findViewById(R.id.text);
        }
    }
}