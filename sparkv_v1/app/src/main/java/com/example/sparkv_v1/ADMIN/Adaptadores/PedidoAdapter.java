package com.example.sparkv_v1.ADMIN.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Actividades.GestionPedidosActivity;
import com.example.sparkv_v1.ADMIN.Clases.Pedido;
import com.example.sparkv_v1.R;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    private final List<Pedido> pedidos;

    public PedidoAdapter(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_adm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);

        holder.tvIdUsuario.setText("ID Usuario: " + pedido.getIdUsuario());
        holder.tvTotal.setText("Total: €" + pedido.getTotal());

        StringBuilder sbItems = new StringBuilder("Servicios:\n");
        for (Pedido.Item item : pedido.getItems()) {
            sbItems.append("- ").append(item.getNombre()).append(" (€").append(item.getPrecio()).append(")\n");
        }
        holder.tvItems.setText(sbItems.toString());

        holder.btnAsignar.setOnClickListener(v -> {
            GestionPedidosActivity activity = (GestionPedidosActivity) holder.itemView.getContext();
            if (activity != null) {
                activity.abrirSeleccionarLimpiador(pedido.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdUsuario, tvTotal, tvItems;
        Button btnAsignar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdUsuario = itemView.findViewById(R.id.tvIdUsuario);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvItems = itemView.findViewById(R.id.tvItems);
            btnAsignar = itemView.findViewById(R.id.btnAsignar);
        }
    }
}