package com.example.sparkv_v1.ADMIN.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sparkv_v1.ADMIN.Adaptadores.UsuarioAdapter;
import com.example.sparkv_v1.ADMIN.Clases.Usuario;
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

public class GestionUsuariosActivity extends AppCompatActivity implements UsuarioAdapter.OnEditClickListener {

    private RecyclerView recyclerViewUsuarios;
    private UsuarioAdapter adapter;
    private List<Usuario> listaUsuarios;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setupDrawer(toolbar);

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        listaUsuarios = new ArrayList<>();
        adapter = new UsuarioAdapter(listaUsuarios, this);
        recyclerViewUsuarios.setAdapter(adapter);

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
            Toast.makeText(this, "Debes iniciar sesión para acceder a esta función.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String role = document.getString("role");
                        if ("administrador".equalsIgnoreCase(role)) {
                            cargarUsuarios();
                        } else {
                            Toast.makeText(this, "Acceso denegado. Debes ser administrador.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Usuario no encontrado en la base de datos.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al verificar rol: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void cargarUsuarios() {
        db.collection("users")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaUsuarios.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String nombre = document.getString("name");
                        String correo = document.getString("email");
                        String rol = document.getString("role");
                        String id = document.getId();

                        if (nombre != null && correo != null && rol != null) {
                            listaUsuarios.add(new Usuario(id, nombre, correo, rol));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error al cargar usuarios", e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onEditClick(Usuario usuario) {
        Intent intent = new Intent(this, EditarUsuarioActivity.class);
        intent.putExtra("usuario", usuario);
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