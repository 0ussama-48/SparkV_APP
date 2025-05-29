package com.example.sparkv_v1.ADMIN.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.PerfilActivity;
import com.example.sparkv_v1.R;
import com.google.android.material.navigation.NavigationView;


public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.abrir_menu, R.string.cerrar_menu);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Manejar clics en el menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_panel_control) {
                startActivity(new Intent(this, PanelControlActivity.class));
            } else if (id == R.id.nav_gestion_usuarios) {
                startActivity(new Intent(this, GestionUsuariosActivity.class));
            } else if (id == R.id.nav_gestion_servicios) {
                startActivity(new Intent(this, GestionServiciosActivity.class));
            } else if (id == R.id.nav_gestion_pedidos) {
                startActivity(new Intent(this, GestionPedidosActivity.class));
            }  else if (id == R.id.nav_reportes_analisis) {
                startActivity(new Intent(this, ReportesActivity.class));
            }  else if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        FrameLayout contenedor = findViewById(R.id.container);
        getLayoutInflater().inflate(obtenerLayoutContenido(), contenedor);
    }

    protected abstract int obtenerLayoutContenido();
}