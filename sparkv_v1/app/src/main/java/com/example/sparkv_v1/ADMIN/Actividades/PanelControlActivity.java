package com.example.sparkv_v1.ADMIN.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.sparkv_v1.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PanelControlActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvTotalUsuarios, tvTotalLimpieza, tvTotalPedidos, tvPedidosActivos, tvPedidosCompletados, tvIngresosTotales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_control);

        // Inicializar componentes
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.abrir_menu,
                R.string.cerrar_menu
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Inicializar TextViews
        tvTotalUsuarios = findViewById(R.id.tvTotalUsuarios);
        tvTotalLimpieza = findViewById(R.id.tvTotalLimpieza);
        tvTotalPedidos = findViewById(R.id.tvTotalPedidos);
        tvPedidosActivos = findViewById(R.id.tvPedidosActivos);
        tvPedidosCompletados = findViewById(R.id.tvPedidosCompletados);
        tvIngresosTotales = findViewById(R.id.tvIngresosTotales);

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
        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").whereEqualTo("role", "cliente").get()
                .addOnSuccessListener(querySnapshot -> {
                    tvTotalUsuarios.setText(querySnapshot.size() + " Clientes");
                })
                .addOnFailureListener(e -> {
                    tvTotalUsuarios.setText("0 Usuarios");
                    Log.e("Admin", "Error al cargar usuarios: ", e);
                });

        // Total de Limpiadores
        db.collection("users").whereEqualTo("role", "limpiador").get()
                .addOnSuccessListener(querySnapshot -> {
                    tvTotalLimpieza.setText(querySnapshot.size() + " Limpiedores");
                })
                .addOnFailureListener(e -> {
                    tvTotalLimpieza.setText("0 Limpieza");
                    Log.e("Admin", "Error al cargar limpiadores: ", e);
                });

        // Total de Pedidos
        db.collection("pedidos_finalizados").get()
                .addOnSuccessListener(querySnapshot -> {
                    tvTotalPedidos.setText(querySnapshot.size() + " Pedidos");
                })
                .addOnFailureListener(e -> {
                    tvTotalPedidos.setText("0 Pedidos");
                    Log.e("Admin", "Error al cargar pedidos: ", e);
                });

        db.collection("pedidos_finalizados").whereEqualTo("estado", "pendiente").get()
                .addOnSuccessListener(querySnapshot -> {
                    tvPedidosActivos.setText(querySnapshot.size() + " Activos");
                })
                .addOnFailureListener(e -> {
                    tvPedidosActivos.setText("0 Activos");
                    Log.e("Admin", "Error al cargar pedidos activos: ", e);
                });

        db.collection("pedidos_finalizados").whereEqualTo("estado", "completado").get()
                .addOnSuccessListener(querySnapshot -> {
                    tvPedidosCompletados.setText(querySnapshot.size() + " Completados");
                })
                .addOnFailureListener(e -> {
                    tvPedidosCompletados.setText("0 Completados");
                    Log.e("Admin", "Error al cargar pedidos completados: ", e);
                });

        db.collection("pedidos_finalizados").get()
                .addOnSuccessListener(querySnapshot -> {
                    double ingresos = 0;
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Double total = document.getDouble("total");
                        if (total != null) {
                            ingresos += total;
                        }
                    }
                    tvIngresosTotales.setText("€" + String.format("%.2f", ingresos));
                })
                .addOnFailureListener(e -> {
                    tvIngresosTotales.setText("€0.00");
                    Log.e("Admin", "Error al calcular ingresos: ", e);
                });
    }
}