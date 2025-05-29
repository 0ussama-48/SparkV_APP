package com.example.sparkv_v1.ADMIN.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Adaptadores.ServicioAdapter;
import com.example.sparkv_v1.ADMIN.Clases.Servicio;
import com.example.sparkv_v1.LoginActivity;
import com.example.sparkv_v1.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GestionServiciosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewServicios;
    private ServicioAdapter adapter;
    private List<Servicio> listaServicios;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_servicios);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setupDrawer(toolbar);

        recyclerViewServicios = findViewById(R.id.recyclerViewServicios);
        recyclerViewServicios.setLayoutManager(new LinearLayoutManager(this));
        listaServicios = new ArrayList<>();
        adapter = new ServicioAdapter(listaServicios, this::onEditClick);
        recyclerViewServicios.setAdapter(adapter);

        // Manejar clics en el menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_panel_control) {
                startActivity(new Intent(this, PanelControlActivity.class));
            }
            if (id == R.id.nav_gestion_usuarios) {
                startActivity(new Intent(this, GestionUsuariosActivity.class));
            }
            if (id == R.id.nav_gestion_servicios) {
                startActivity(new Intent(this, GestionServiciosActivity.class));
            }
            if (id == R.id.nav_gestion_pedidos) {
                startActivity(new Intent(this, GestionPedidosActivity.class));
            }
            if (id == R.id.nav_reportes_analisis) {
                startActivity(new Intent(this, ReportesActivity.class));
            }
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        verificarRol();
    }

    private void verificarRol() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión como administrador.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists() && "administrador".equals(document.getString("role"))) {
                        cargarServicios();
                    } else {
                        Toast.makeText(this, "Acceso denegado. No eres administrador.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al verificar rol: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void cargarServicios() {
        db.collection("servicios_populares").get()
                .addOnSuccessListener(querySnapshot -> {
                    listaServicios.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String nombre = document.getString("nombre");
                        String descripcion = document.getString("descripcion");
                        Double precio = document.getDouble("precio");

                        if (nombre != null && descripcion != null && precio != null) {
                            listaServicios.add(new Servicio(document.getId(), nombre, descripcion, precio));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar servicios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void onEditClick(Servicio servicio) {
        Intent intent = new Intent(this, EditarServicioActivity.class);
        intent.putExtra("servicios_populares", servicio);
        startActivity(intent);
    }

    private void setupDrawer(MaterialToolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.abrir_menu,
                R.string.cerrar_menu
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_gestion_usuarios) {
                startActivity(new Intent(this, GestionUsuariosActivity.class));
            } else if (id == R.id.nav_panel_control) {
                startActivity(new Intent(this, PanelControlActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}