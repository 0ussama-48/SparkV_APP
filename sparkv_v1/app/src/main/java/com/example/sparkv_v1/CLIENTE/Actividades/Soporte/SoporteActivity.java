package com.example.sparkv_v1.CLIENTE.Actividades.Soporte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sparkv_v1.CLIENTE.Actividades.Carrito.CarritoActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Home.ClienteMainActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.MisReservasActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.PerfilActivity;
import com.example.sparkv_v1.CLIENTE.Adaptadores.FaqAdaptador;
import com.example.sparkv_v1.CLIENTE.Clases.FaqDomain;
import com.example.sparkv_v1.R;

import java.util.ArrayList;
import java.util.List;


public class SoporteActivity extends AppCompatActivity {

    private RecyclerView faqRecyclerView;
    private FaqAdaptador faqAdaptador;
    private List<FaqDomain> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqList = new ArrayList<>();
        faqAdaptador = new FaqAdaptador(faqList, this);

        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqRecyclerView.setAdapter(faqAdaptador);


        // Botón de Inicio
        View inicioBtn = findViewById(R.id.inicioBtn);
        inicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoporteActivity.this, ClienteMainActivity.class);
                startActivity(intent);
            }
        });

        // Botón de Perfil
        View perfilBtn = findViewById(R.id.PerfilBtn);
        perfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoporteActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        // Botón de carrito
        View carritoBtn = findViewById(R.id.fabCarrito);
        carritoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoporteActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });

        // Botón de Soporte
        View soporteBtn = findViewById(R.id.soporteBtn);
        soporteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoporteActivity.this, SoporteActivity.class);
                startActivity(intent);
            }
        });
        // Botón de reservas
        View reservasBtn = findViewById(R.id.reservasBtn);
        if (reservasBtn != null) {
            reservasBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SoporteActivity.this, MisReservasActivity.class);
                startActivity(intent);
            });
        }
        loadFaqsFromLocal();
    }

    private void loadFaqsFromLocal() {
        faqList.clear();
        faqList.addAll(obtenerDatosLocales());
        faqAdaptador.notifyDataSetChanged();
    }

    private List<FaqDomain> obtenerDatosLocales() {
        List<FaqDomain> faqList = new ArrayList<>();
        faqList.add(new FaqDomain("¿Cómo reservo un servicio?", "Desde la sección 'Reservar', selecciona la fecha, hora y ubicación."));
        faqList.add(new FaqDomain("¿Qué tipos de servicios ofrecen?", "Ofrecemos limpieza básica, premium y detailing."));
        faqList.add(new FaqDomain("¿Es seguro pagar a través de la app?", "Sí, utilizamos métodos de pago seguros como tarjeta de crédito y PayPal."));
        faqList.add(new FaqDomain("¿Puedo cancelar una reserva?", "Sí, puedes cancelar una reserva hasta 24 horas antes del servicio."));
        return faqList;
    }

    // Método para el botón "Enviar Solicitud"
    public void PoliticaPrivacidad(android.view.View view) {
        Intent intent = new Intent(this, PoliticaPrivacidadActividad.class);
        startActivity(intent);
    }

    // Método para el botón "Reportar Problema"
    public void openReportProblemActivity(android.view.View view) {
        Intent intent = new Intent(this, ReportarProblemaActividad.class);
        startActivity(intent);
    }
}