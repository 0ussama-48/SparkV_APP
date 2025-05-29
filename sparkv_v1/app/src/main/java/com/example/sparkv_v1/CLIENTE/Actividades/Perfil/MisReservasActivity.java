package com.example.sparkv_v1.CLIENTE.Actividades.Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Actividades.Carrito.CarritoActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Home.ClienteMainActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Soporte.SoporteActivity;
import com.example.sparkv_v1.CLIENTE.Adaptadores.ReservasAdaptador;
import com.example.sparkv_v1.CLIENTE.Clases.ReservaDomain;
import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MisReservasActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerViewReservas;
    private ReservasAdaptador adapter;
    private ArrayList<ReservaDomain> listaReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Botón de Inicio
        View inicioBtn = findViewById(R.id.inicioBtn);
        inicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisReservasActivity.this, ClienteMainActivity.class);
                startActivity(intent);
            }
        });


        // Botón de Perfil
        View perfilBtn = findViewById(R.id.PerfilBtn);
        perfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisReservasActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        // Botón de carrito
        View carritoBtn = findViewById(R.id.fabCarrito);
        carritoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisReservasActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });

        // Botón de Soporte
        View soporteBtn = findViewById(R.id.soporteBtn);
        soporteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisReservasActivity.this, SoporteActivity.class);
                startActivity(intent);
            }
        });
        // Botón de reservas
        View reservasBtn = findViewById(R.id.reservasBtn);
        if (reservasBtn != null) {
            reservasBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MisReservasActivity.this, MisReservasActivity.class);
                startActivity(intent);
            });
        }


        configurarToolbar();
        inicializarRecyclerView();
        cargarReservas();
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void inicializarRecyclerView() {
        listaReservas = new ArrayList<>();
        adapter = new ReservasAdaptador(listaReservas);
        recyclerViewReservas = findViewById(R.id.recyclerViewReservas);
        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReservas.setAdapter(adapter);
    }

    private void cargarReservas() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        TextView tvSinReservas = findViewById(R.id.tvSinReservas);

        firestore.collection("pedidos_finalizados")
                .whereEqualTo("usuarioId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaReservas.clear();
                    if (querySnapshot.isEmpty()) {
                        tvSinReservas.setVisibility(View.VISIBLE);
                    } else {
                        tvSinReservas.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Object itemsObj = document.get("items");
                            if (itemsObj instanceof ArrayList) {
                                ArrayList<?> itemsList = (ArrayList<?>) itemsObj;
                                for (Object itemObj : itemsList) {
                                    if (itemObj instanceof Map) {
                                        Map<String, Object> item = (Map<String, Object>) itemObj;
                                        String nombre = (String) item.get("nombre");
                                        String fecha = (String) item.get("fecha");
                                        String hora = (String) item.get("hora");
                                        String categoria = (String) item.get("categoria");

                                        if (nombre != null) {
                                            listaReservas.add(new ReservaDomain(
                                                    nombre,
                                                    fecha != null ? fecha : "Fecha no disponible",
                                                    hora != null ? hora : "Hora no disponible",
                                                    categoria != null ? categoria : "Sin categoría"
                                            ));
                                        }
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar reservas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al cargar reservas: ", e);
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}