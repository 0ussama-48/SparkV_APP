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

import com.example.sparkv_v1.ADMIN.Adaptadores.ReporteAdapter;
import com.example.sparkv_v1.ADMIN.Clases.Reporte;
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

public class ReportesActivity extends AppCompatActivity {
    private RecyclerView recyclerViewReportes;
    private ReporteAdapter adapter;
    private List<Reporte> listaReportes;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_adm);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setupDrawer(toolbar);

        recyclerViewReportes = findViewById(R.id.recyclerViewReportes);
        recyclerViewReportes.setLayoutManager(new LinearLayoutManager(this));
        listaReportes = new ArrayList<>();
        adapter = new ReporteAdapter(listaReportes, this::onEditClick);
        recyclerViewReportes.setAdapter(adapter);
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
                        cargarReportes();
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

    private void cargarReportes() {
        db.collection("reportes_problemas").get()
                .addOnSuccessListener(querySnapshot -> {
                    listaReportes.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String descripcion = document.getString("descripcion");
                        String userId = document.getString("userId");
                        String userName = document.getString("userName");
                        String fecha = document.getString("fecha");

                        if (descripcion != null && userId != null && userName != null && fecha != null) {
                            listaReportes.add(new Reporte(document.getId(), descripcion, userId, userName, fecha));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar reportes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void onEditClick(Reporte reporte) {
        Intent intent = new Intent(this, EditarReporteActivity.class);
        intent.putExtra("reporte", reporte);
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