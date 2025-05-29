package com.example.sparkv_v1.CLIENTE.Actividades.Soporte;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.R;

public class PoliticaPrivacidadActividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);

        // Configurar el título de la barra superior
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Política de Privacidad");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView titulo = findViewById(R.id.titulo);
        titulo.setAlpha(0f);
        titulo.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}