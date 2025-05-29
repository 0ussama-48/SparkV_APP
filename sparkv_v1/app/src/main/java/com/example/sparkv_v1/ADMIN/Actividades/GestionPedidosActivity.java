package com.example.sparkv_v1.ADMIN.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Adaptadores.PedidoAdapter;
import com.example.sparkv_v1.ADMIN.Clases.Pedido;
import com.example.sparkv_v1.LoginActivity;
import com.example.sparkv_v1.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestionPedidosActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPedidos;
    private PedidoAdapter adapter;
    private List<Pedido> listaPedidos;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_pedidos);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.abrir_menu,
                R.string.cerrar_menu
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        listaPedidos = new ArrayList<>();
        adapter = new PedidoAdapter(listaPedidos);
        recyclerViewPedidos.setAdapter(adapter);
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
                        cargarPedidos();
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

    private void cargarPedidos() {
        db.collection("pedidos_finalizados").get()
                .addOnSuccessListener(querySnapshot -> {
                    listaPedidos.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String idUsuario = document.getString("usuarioId");
                        Double total = document.getDouble("total");

                        Object itemsObj = document.get("items");
                        List<Pedido.Item> items = new ArrayList<>();
                        if (itemsObj instanceof List<?>) {
                            for (Object item : (List<?>) itemsObj) {
                                if (item instanceof Map<?, ?>) {
                                    Map<String, Object> itemMap = (Map<String, Object>) item;
                                    Pedido.Item servicio = new Pedido.Item(
                                            (String) itemMap.get("nombre"),
                                            (Double) itemMap.get("precio"),
                                            (String) itemMap.get("categoria"),
                                            (String) itemMap.get("duracion"),
                                            (String) itemMap.get("servicioId")
                                    );
                                    items.add(servicio);
                                }
                            }
                        }

                        String idLimpiador = document.getString("idLimpiador") != null ? document.getString("idLimpiador") : "";
                        if (idUsuario != null && total != null) {
                            listaPedidos.add(new Pedido(document.getId(), idUsuario, total, items, idLimpiador));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar pedidos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Admin", "Error al cargar pedidos: ", e);
                });
    }

    public void abrirSeleccionarLimpiador(String idPedido) {
        db.collection("pedidos_asignados")
                .whereEqualTo("idPedido", idPedido)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // El pedido ya está asignado, eliminar la entrada anterior
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Asignación anterior eliminada", Toast.LENGTH_SHORT).show();
                                        asignarNuevoLimpiador(idPedido);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al eliminar asignación anterior: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("Admin", "Error al eliminar asignación anterior: ", e);
                                    });
                        }
                    } else {
                        // No hay asignación previa, proceder a asignar nuevo limpiador
                        asignarNuevoLimpiador(idPedido);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar asignaciones anteriores: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Admin", "Error al buscar asignaciones anteriores: ", e);
                });
    }

    private void asignarNuevoLimpiador(String idPedido) {
        Intent intent = new Intent(this, SeleccionarLimpiadorActivity.class);
        intent.putExtra("pedidoId", idPedido);
        startActivity(intent);
    }
}