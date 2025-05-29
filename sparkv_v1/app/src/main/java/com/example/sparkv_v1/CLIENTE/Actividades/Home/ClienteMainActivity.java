package com.example.sparkv_v1.CLIENTE.Actividades.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Actividades.Carrito.CarritoActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.MisReservasActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Soporte.SoporteActivity;
import com.example.sparkv_v1.CLIENTE.Adaptadores.CategoriasAdaptador;
import com.example.sparkv_v1.CLIENTE.Adaptadores.PopularAdaptador;
import com.example.sparkv_v1.CLIENTE.Clases.PopularDomain;
import com.example.sparkv_v1.CLIENTE.Clases.CategoriaDomain;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.PerfilActivity;
import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ClienteMainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewListaCategoria, recyclerViewListaPopular;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String role = getIntent().getStringExtra("role");

        if ("client".equals(role)) {
            Toast.makeText(this, "Bienvenido, Cliente", Toast.LENGTH_SHORT).show();
        } else if ("cleaner".equals(role)) {
            Toast.makeText(this, "Bienvenido, Experto en Limpieza", Toast.LENGTH_SHORT).show();
        }

        // Botón de Inicio
        View inicioBtn = findViewById(R.id.inicioBtn);
        inicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteMainActivity.this, ClienteMainActivity.class);
                startActivity(intent);
            }
        });


        // Botón de Perfil
        View perfilBtn = findViewById(R.id.PerfilBtn);
        perfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteMainActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        // Botón de carrito
        View carritoBtn = findViewById(R.id.fabCarrito);
        carritoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteMainActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });

        // Botón de Soporte
        View soporteBtn = findViewById(R.id.soporteBtn);
        soporteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteMainActivity.this, SoporteActivity.class);
                startActivity(intent);
            }
        });
        // Botón de reservas
        View reservasBtn = findViewById(R.id.reservasBtn);
        if (reservasBtn != null) {
            reservasBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ClienteMainActivity.this, MisReservasActivity.class);
                startActivity(intent);
            });
        }

        // Configuración de RecyclerViews
        recyclerViewListaCategoria();
        insertarDatosIniciales();
        recyclerViewPopular();
    }

    // Método para configurar las categorías
    private void recyclerViewListaCategoria() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListaCategoria = findViewById(R.id.recyclerView);
        recyclerViewListaCategoria.setLayoutManager(linearLayoutManager);

        ArrayList<CategoriaDomain> categorias = new ArrayList<>();
        categorias.add(new CategoriaDomain("Lavado Exterior", "cat_lavado_exterior"));
        categorias.add(new CategoriaDomain("Lavado Interior", "cat_lavado_interior"));
        categorias.add(new CategoriaDomain("Tratamiento Tapicerías", "cat_tapicerias"));
        categorias.add(new CategoriaDomain("Encerado y Pulido", "cat_encerado_pulido"));
        categorias.add(new CategoriaDomain("Revisión Fluidos", "cat_revision_fluidos"));

        adapter = new CategoriasAdaptador(categorias);
        recyclerViewListaCategoria.setAdapter(adapter);
    }

    private void recyclerViewPopular() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListaPopular = findViewById(R.id.recyclerView2);
        recyclerViewListaPopular.setLayoutManager(layoutManager);

        ArrayList<PopularDomain> mejoresServicios = new ArrayList<>();
        adapter2 = new PopularAdaptador(mejoresServicios);
        recyclerViewListaPopular.setAdapter(adapter2);

        db.collection("servicios_populares").get()
                .addOnSuccessListener(querySnapshot -> {
                    mejoresServicios.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String nombre = document.getString("nombre");
                        String imagen = document.getString("imagen");
                        String descripcion = document.getString("descripcion");
                        Double precio = document.getDouble("precio");
                        String categoria = document.getString("categoria");
                        String duracion = document.getString("duracion");
                        String detallesAdicionales = document.getString("detallesAdicionales");

                        if (nombre != null && imagen != null && precio != null) {
                            mejoresServicios.add(new PopularDomain(
                                    nombre,
                                    imagen,
                                    descripcion != null ? descripcion : "Descripción no disponible",
                                    precio,
                                    0,
                                    categoria != null ? categoria : "Sin categoría",
                                    duracion != null ? duracion : "N/A",
                                    detallesAdicionales != null ? detallesAdicionales : "No hay detalles",
                                    document.getId()
                            ));
                        }
                    }
                    adapter2.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar servicios populares: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al obtener servicios populares", e);
                });
    }
    private void insertarDatosIniciales() {
        db.collection("servicios_populares").get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        // Colección vacía, insertar datos de prueba
                        ArrayList<HashMap<String, Object>> serviciosPopulares = new ArrayList<>();

                        HashMap<String, Object> servicio1 = new HashMap<>();
                        servicio1.put("nombre", "Lavado Premium");
                        servicio1.put("imagen", "pop_lavado_premium");
                        servicio1.put("descripcion", "Lavado completo: exterior e interior, con productos ecológicos.");
                        servicio1.put("precio", 19.99);
                        servicio1.put("categoria", "Lavado");
                        servicio1.put("duracion", "1 hora");
                        servicio1.put("detallesAdicionales", "Incluye encerado y limpieza de interiores");

                        HashMap<String, Object> servicio2 = new HashMap<>();
                        servicio2.put("nombre", "Pulido Profesional");
                        servicio2.put("imagen", "pop_pulido_profesional");
                        servicio2.put("descripcion", "Encerado y pulido de alta calidad para un acabado brillante.");
                        servicio2.put("precio", 29.99);
                        servicio2.put("categoria", "Mantenimiento");
                        servicio2.put("duracion", "2 horas");
                        servicio2.put("detallesAdicionales", "Ideal para vehículos con acabados brillantes");

                        HashMap<String, Object> servicio3 = new HashMap<>();
                        servicio3.put("nombre", "Limpieza de Tapicerías");
                        servicio3.put("imagen", "pop_tapicerias");
                        servicio3.put("descripcion", "Tratamiento especializado para asientos de cuero o tela.");
                        servicio3.put("precio", 34.99);
                        servicio3.put("categoria", "Tapicerías");
                        servicio3.put("duracion", "1.5 horas");
                        servicio3.put("detallesAdicionales", "Limpieza profunda y protección de asientos");

                        HashMap<String, Object> servicio4 = new HashMap<>();
                        servicio4.put("nombre", "Mantenimiento Básico");
                        servicio4.put("imagen", "pop_mantenimiento_basico");
                        servicio4.put("descripcion", "Revisión de fluidos y ajustes menores para tu vehículo.");
                        servicio4.put("precio", 14.99);
                        servicio4.put("categoria", "Mantenimiento");
                        servicio4.put("duracion", "1 hora");
                        servicio4.put("detallesAdicionales", "Revisión de aceite, frenos y niveles de líquidos");

                        HashMap<String, Object> servicio5 = new HashMap<>();
                        servicio5.put("nombre", "Lavado Ecológico");
                        servicio5.put("imagen", "pop_lavado_ecologico");
                        servicio5.put("descripcion", "Limpieza sostenible con productos biodegradables.");
                        servicio5.put("precio", 17.99);
                        servicio5.put("categoria", "Lavado");
                        servicio5.put("duracion", "1 hora");
                        servicio5.put("detallesAdicionales", "Sin productos químicos agresivos");

                        serviciosPopulares.add(servicio1);
                        serviciosPopulares.add(servicio2);
                        serviciosPopulares.add(servicio3);
                        serviciosPopulares.add(servicio4);
                        serviciosPopulares.add(servicio5);

                        // Subir datos a Firestore
                        for (HashMap<String, Object> servicio : serviciosPopulares) {
                            db.collection("servicios_populares").add(servicio)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("Firestore", "Servicio añadido con ID: " + documentReference.getId());
                                        recyclerViewPopular(); // Recargar datos
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al insertar datos iniciales: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("Firestore", "Error al insertar datos iniciales", e);
                                    });
                        }
                    } else {
                        recyclerViewPopular();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al verificar datos iniciales: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al verificar datos iniciales", e);
                });
    }
}