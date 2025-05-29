package com.example.sparkv_v1.LIMPIADOR.Actividades;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Adaptadores.TaskAdapter;
import com.example.sparkv_v1.CLIENTE.Clases.TaskDomain;
import com.example.sparkv_v1.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.maps.CameraUpdateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LimpiadorMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ArrayList<TaskDomain> tasks = new ArrayList<>();
    private static final double ZARAGOZA_LAT = 41.6500;
    private static final double ZARAGOZA_LON = -0.8833;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpiador_main);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setupRecyclerView();
        setupMap();
        fetchAssignedOrders();
    }


    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            throw new RuntimeException("Fragmento de mapa no encontrado en el diseño.");
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerViewTasks = findViewById(R.id.taskSummary);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        TaskAdapter taskAdapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        recyclerViewTasks.setAlpha(0f);
        recyclerViewTasks.animate().alpha(1f).setDuration(500).start();
    }

    private void fetchAssignedOrders() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("pedidos_asignados")
                .whereEqualTo("idLimpiador", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tasks.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String idPedido = document.getString("idPedido");

                            if (idPedido != null) {
                                db.collection("pedidos_finalizados").document(idPedido)
                                        .get()
                                        .addOnSuccessListener(pedidoDoc -> {
                                            if (pedidoDoc.exists()) {
                                                Map<String, Object> data = pedidoDoc.getData();

                                                String direccion = data.get("direccion") != null ? data.get("direccion").toString() : "Sin dirección";
                                                String fecha = data.get("fecha") != null ? data.get("fecha").toString() : "Fecha no disponible";
                                                String hora = data.get("hora") != null ? data.get("hora").toString() : "Hora no disponible";

                                                TaskDomain taskDomain = new TaskDomain(
                                                        "Lavado",
                                                        (Double) data.get("total"),
                                                        direccion,
                                                        fecha,
                                                        hora
                                                );

                                                tasks.add(taskDomain);

                                                RecyclerView recyclerViewTasks = findViewById(R.id.taskSummary);
                                                TaskAdapter adapter = (TaskAdapter) recyclerViewTasks.getAdapter();
                                                if (adapter != null) {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                Log.e("Admin", "Documento de pedido no existe: " + idPedido);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Admin", "Error al obtener detalles del pedido: " + e.getMessage());
                                        });
                            } else {
                                Log.e("Admin", "ID del pedido es nulo");
                            }
                        }
                    } else {
                        Log.e("Admin", "Error al obtener pedidos asignados: ", task.getException());
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        CargarLocalizacionesEnMapa();
        LatLng zaragoza = new LatLng(ZARAGOZA_LAT, ZARAGOZA_LON);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 12));
    }
    private void CargarLocalizacionesEnMapa() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("pedidos_asignados")
                .whereEqualTo("idLimpiador", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String idPedido = document.getString("idPedido");

                            if (idPedido != null) {
                                db.collection("pedidos_finalizados").document(idPedido)
                                        .get()
                                        .addOnSuccessListener(pedidoDoc -> {
                                            if (pedidoDoc.exists()) {
                                                String direccion = pedidoDoc.getString("direccion");
                                                if (direccion != null) {
                                                    LatLng location = getLocationFromAddress(direccion);
                                                    if (location != null) {
                                                        mMap.addMarker(new MarkerOptions()
                                                                .position(location)
                                                                .title("Cliente")
                                                                .snippet(direccion));
                                                    }
                                                }
                                            } else {
                                                Log.e("Admin", "Documento de pedido no existe: " + idPedido);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Admin", "Error al obtener detalles del pedido: " + e.getMessage());
                                        });
                            } else {
                                Log.e("Admin", "ID del pedido es nulo");
                            }
                        }
                    } else {
                        Log.e("Admin", "Error al obtener pedidos asignados: ", task.getException());
                    }
                });
    }
    private LatLng getLocationFromAddress(String addressString) {
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (Exception e) {
            Log.e("Admin", "Error al obtener ubicación: ", e);
        }
        return null;
    }

    /**
     * Acción al hacer clic en el botón "Inicio".
     */
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, LimpiadorMainActivity.class);
        startActivity(intent);
    }

    /**
     * Acción al hacer clic en el botón "Perfil".
     */
    public void onProfileClick(View view) {
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    /**
     * Acción al hacer clic en el botón "Mis Tareas".
     */
    public void onTasksClick(View view) {
        Intent intent = new Intent(this, MisTareasActivity.class);
        startActivity(intent);
    }

    /**
     * Acción al hacer clic en el botón "Historial".
     */
    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, HistorialActivity.class);
        startActivity(intent);
    }
}