package com.example.sparkv_v1.ADMIN.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sparkv_v1.ADMIN.Clases.Usuario;
import com.example.sparkv_v1.R;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private final List<Usuario> listaUsuarios;
    private OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEditClick(Usuario usuario);
    }

    public UsuarioAdapter(List<Usuario> listaUsuarios, OnEditClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.textViewNombre.setText(usuario.getNombre());
        holder.textViewCorreo.setText(usuario.getCorreo());
        holder.textViewRol.setText("Rol: " + usuario.getRol());

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(usuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewCorreo, textViewRol;
        Button btnEditar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewCorreo = itemView.findViewById(R.id.textViewCorreo);
            textViewRol = itemView.findViewById(R.id.textViewRol);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}