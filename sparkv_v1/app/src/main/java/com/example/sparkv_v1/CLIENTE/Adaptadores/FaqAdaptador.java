package com.example.sparkv_v1.CLIENTE.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.R;
import com.example.sparkv_v1.CLIENTE.Clases.FaqDomain;

import java.util.List;

public class FaqAdaptador extends RecyclerView.Adapter<FaqAdaptador.ViewHolder> {

    private List<FaqDomain> faqList;
    private Context context;

    public FaqAdaptador(List<FaqDomain> faqList, Context context) {
        this.faqList = faqList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FaqDomain faq = faqList.get(position);
        holder.pregunta.setText(faq.getPregunta());
        holder.respuesta.setText(faq.getRespuesta());
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pregunta, respuesta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pregunta = itemView.findViewById(R.id.pregunta);
            respuesta = itemView.findViewById(R.id.respuesta);
        }
    }
}