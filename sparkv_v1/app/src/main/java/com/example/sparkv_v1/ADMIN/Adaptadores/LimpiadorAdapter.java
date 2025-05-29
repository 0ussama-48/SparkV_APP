package com.example.sparkv_v1.ADMIN.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Clases.Limpiador;
import com.example.sparkv_v1.R;

import java.util.List;

public class LimpiadorAdapter extends RecyclerView.Adapter<LimpiadorAdapter.ViewHolder> {
    private final List<Limpiador> limpiadores;
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(String cleanerId);
    }

    public LimpiadorAdapter(List<Limpiador> limpiadores, OnItemSelectedListener listener) {
        this.limpiadores = limpiadores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_limpiador_adm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Limpiador limpiador = limpiadores.get(position);
        holder.email.setText(limpiador.getEmail());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemSelected(limpiador.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return limpiadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.tvLimpiadorEmail);
        }
    }
}
