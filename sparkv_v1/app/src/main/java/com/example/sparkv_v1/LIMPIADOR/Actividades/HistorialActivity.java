package com.example.sparkv_v1.LIMPIADOR.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.LIMPIADOR.Adaptadores.TareaAdapter;
import com.example.sparkv_v1.LIMPIADOR.Clases.Pedido;
import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class HistorialActivity extends AppCompatActivity implements TareaAdapter.OnItemClickListener {
    private RecyclerView recyclerViewHistorial;
    private TareaAdapter adapter;
    private ArrayList<Pedido> listaHistorial;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewHistorial = findViewById(R.id.recyclerViewHistorial);
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
        listaHistorial = new ArrayList<>();
        adapter = new TareaAdapter(listaHistorial, this);
        recyclerViewHistorial.setAdapter(adapter);

        cargarPedidosHistorial();
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

    private void cargarPedidosHistorial() {
        String userId = mAuth.getCurrentUser().getUid();

        Log.d("Historial", "UID del usuario autenticado: " + userId);

        db.collection("historial_pedidos")
                .whereEqualTo("idLimpiador", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d("Historial", "Número de documentos recuperados: " + querySnapshot.size());

                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Object itemsRaw = document.get("items");
                            ArrayList<Pedido.Item> itemsList = new ArrayList<>();

                            if (itemsRaw instanceof ArrayList<?>) {
                                for (Object item : (ArrayList<?>) itemsRaw) {
                                    if (item instanceof Map<?, ?>) {
                                        Map<String, Object> mapItem = (Map<String, Object>) item;
                                        Pedido.Item servicio = new Pedido.Item(
                                                (String) mapItem.get("nombre"),
                                                (Double) mapItem.get("precio"),
                                                (String) mapItem.get("categoria"),
                                                (String) mapItem.get("duracion"),
                                                (String) mapItem.get("servicioId")
                                        );
                                        itemsList.add(servicio);
                                    }
                                }
                            }

                            String idUsuario = document.getString("idUsuario");
                            Double total = document.getDouble("total");

                            if (idUsuario != null && total != null) {
                                listaHistorial.add(new Pedido(
                                        document.getId(),
                                        idUsuario,
                                        total,
                                        itemsList,
                                        userId
                                ));
                                Log.d("Historial", "Pedido agregado: " + document.getId());
                            } else {
                                Log.e("Historial", "Datos incompletos en documento: " + document.getId());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Log.d("Historial", "Adapter actualizado con " + listaHistorial.size() + " elementos");
                    } else {
                        TextView tvSinPedidos = findViewById(R.id.tvSinPedidos);
                        tvSinPedidos.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "No tienes pedidos en el historial.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar historial: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Admin", "Error al obtener pedidos históricos: ", e);
                });
    }

    @Override
    public void onItemClick(Pedido pedido) {
        mostrarDetallesPedido(pedido);
    }

    private void mostrarDetallesPedido(Pedido pedido) {
        Intent intent = new Intent(HistorialActivity.this, DetallePedidoActivity.class);
        intent.putExtra("pedido", (Serializable) pedido);
        startActivity(intent);
    }
}