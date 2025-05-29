package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sparkv_v1.CLIENTE.Clases.CategoriaDomain;
import com.example.sparkv_v1.R;

import java.util.ArrayList;

public class CategoriasAdaptador extends RecyclerView.Adapter<CategoriasAdaptador.ViewHolder> {
    ArrayList<CategoriaDomain> categoriaDomains;

    public CategoriasAdaptador(ArrayList<CategoriaDomain> categoriaDomains) {
        this.categoriaDomains = categoriaDomains;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_categoria,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nombreCategoria.setText(categoriaDomains.get(position).getTitulo());
        String picUrl = "";
        switch (position){
            case 0:{
                picUrl ="cat_lavado_exterior";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.cat_background1));
                break;
            }
            case 1:{
                picUrl ="cat_lavado_interior";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.cat_background2));
                break;
            }
            case 2:{
                picUrl ="cat_tapicerias";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.cat_background3));
                break;
            }
            case 3:{
                picUrl ="cat_encerado_pulido";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.cat_background4));
                break;
            }
            case 4:{
                picUrl ="cat_revision_fluidos";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.cat_background5));
                break;
            }
        }
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable",holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.picCategoria);
    }

    @Override
    public int getItemCount() {

        return categoriaDomains.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreCategoria;
        ImageView picCategoria;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoria = itemView.findViewById(R.id.nombreCategoria);
            picCategoria = itemView.findViewById(R.id.picCategoria);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
