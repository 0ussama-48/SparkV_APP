package com.example.sparkv_v1.LIMPIADOR.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MisTareasActivity extends AppCompatActivity implements TareaAdapter.OnItemClickListener {

    private RecyclerView recyclerViewTareas;
    private TareaAdapter adapter;
    private ArrayList<Pedido> tareasAsignadas = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tareas);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TareaAdapter(tareasAsignadas, this);
        recyclerViewTareas.setAdapter(adapter);

        cargarPedidosAsignados();


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
    private void cargarPedidosAsignados() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("pedidos_asignados")
                .whereEqualTo("idLimpiador", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String idPedido = document.getString("idPedido");

                            if (idPedido != null) {
                                db.collection("pedidos_finalizados").document(idPedido)
                                        .get()
                                        .addOnSuccessListener(pedidoDoc -> {
                                            if (pedidoDoc.exists()) {
                                                Object itemsRaw = pedidoDoc.get("items");
                                                ArrayList<Pedido.Item> itemsList = null;

                                                if (itemsRaw instanceof ArrayList<?>) {
                                                    ArrayList<?> rawItems = (ArrayList<?>) itemsRaw;
                                                    itemsList = new ArrayList<>();

                                                    for (Object item : rawItems) {
                                                        if (item instanceof Map) {
                                                            Map<String, Object> mapItem = (Map<String, Object>) item;

                                                            String nombre = (String) mapItem.get("nombre");
                                                            Double precio = (Double) mapItem.get("precio");
                                                            String categoria = (String) mapItem.get("categoria");
                                                            String duracion = (String) mapItem.get("duracion");
                                                            String servicioId = (String) mapItem.get("servicioId");

                                                            Pedido.Item itemConverted = new Pedido.Item(nombre,precio,categoria,duracion,servicioId);
                                                            itemsList.add(itemConverted);
                                                        }
                                                    }
                                                } else {
                                                    Log.e("Admin", "El campo 'items' no es una lista.");
                                                }

                                                Pedido pedido = new Pedido(
                                                        idPedido,
                                                        pedidoDoc.getString("usuarioId"),
                                                        pedidoDoc.getDouble("total"),
                                                        itemsList,
                                                        pedidoDoc.getString("idLimpiador")
                                                );

                                                if (pedido != null) {
                                                    tareasAsignadas.add(pedido);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                Log.e("Admin", "Documento de pedido no existe: " + idPedido);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Admin", "Error al obtener detalles del pedido: " + e.getMessage());
                                        });
                            }
                        }
                    } else {
                        Toast.makeText(this, "No tienes tareas asignadas.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Admin", "Error al obtener pedidos asignados: ", e);
                    Toast.makeText(this, "Error al cargar tareas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemClick(Pedido pedido) {
        mostrarDetallesPedido(pedido);
    }

    private void mostrarDetallesPedido(Pedido pedido) {
        Intent intent = new Intent(MisTareasActivity.this, DetallePedidoActivity.class);
        intent.putExtra("pedido", (Serializable) pedido);
        startActivity(intent);
    }
}