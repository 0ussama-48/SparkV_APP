package com.example.sparkv_v1.LIMPIADOR.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.R;
import com.example.sparkv_v1.LIMPIADOR.Clases.Pedido;

import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private ArrayList<Pedido> listaTareas;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pedido pedido);
    }

    public TareaAdapter(ArrayList<Pedido> listaTareas, OnItemClickListener listener) {
        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido_lista, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Pedido pedido = listaTareas.get(position);

        StringBuilder itemsText = new StringBuilder();
        for (Pedido.Item item : pedido.getItems()) {
            itemsText.append("- ").append(item.getNombre()).append(": €").append(item.getPrecio()).append("\n");
        }

        holder.tvUsuario.setText("ID Usuario: " + pedido.getIdUsuario());
        holder.tvTotal.setText("Total: €" + pedido.getTotal());
        holder.tvItems.setText(itemsText.toString());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position < listaTareas.size()) {
                listener.onItemClick(pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsuario, tvTotal, tvItems;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvItems = itemView.findViewById(R.id.tvItems);
        }
    }
}