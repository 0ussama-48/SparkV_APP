package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sparkv_v1.CLIENTE.Actividades.Home.MostrarDetallesActivity;
import com.example.sparkv_v1.CLIENTE.Clases.PopularDomain;
import com.example.sparkv_v1.R;

import java.util.ArrayList;

public class PopularAdaptador extends RecyclerView.Adapter<PopularAdaptador.ViewHolder> {
    private final ArrayList<PopularDomain> popularesDomains;

    public PopularAdaptador(ArrayList<PopularDomain> popularesDomains) {
        this.popularesDomains = popularesDomains;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularDomain item = popularesDomains.get(position);

        holder.titulo.setText(item.getTitle() != null ? item.getTitle() : "Servicio no disponible");
        holder.fee.setText(item.getFee() != null ? String.valueOf(item.getFee()) : "0.0 €");
        holder.categoriaTxt.setText("Categoría: " + (item.getCategoria() != null ? item.getCategoria() : "Sin categoría"));
        holder.duracionTxt.setText("Duración: " + (item.getDuracion() != null ? item.getDuracion() : "N/A"));
        holder.detallesAdicionalesTxt.setText("Detalles: " + (item.getDetallesAdicionales() != null ? item.getDetallesAdicionales() : "No hay detalles"));

        String resourceName = item.getPic();
        if (resourceName != null && !resourceName.isEmpty()) {
            int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(
                    resourceName,
                    "drawable",
                    holder.itemView.getContext().getPackageName()
            );
            if (drawableResourceId != 0) {
                Glide.with(holder.itemView.getContext())
                        .load(drawableResourceId)
                        .into(holder.pic);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.person_24)
                        .into(holder.pic);
            }
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.person_24)
                    .into(holder.pic);
        }

        // Navegar a MostrarDetallesActivity
        holder.anadirBtn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MostrarDetallesActivity.class);
            intent.putExtra("object", item);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return popularesDomains.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fee, anadirBtn, categoriaTxt, duracionTxt, detallesAdicionalesTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            fee = itemView.findViewById(R.id.fee);
            pic = itemView.findViewById(R.id.pic);
            anadirBtn = itemView.findViewById(R.id.anadirBtn);
            categoriaTxt = itemView.findViewById(R.id.categoriaTxt);
            duracionTxt = itemView.findViewById(R.id.duracionTxt);
            detallesAdicionalesTxt = itemView.findViewById(R.id.detallesAdicionalesTxt);
        }
    }
}