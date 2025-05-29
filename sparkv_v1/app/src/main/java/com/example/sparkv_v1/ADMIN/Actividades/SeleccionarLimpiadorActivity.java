package com.example.sparkv_v1.ADMIN.Actividades;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.ADMIN.Adaptadores.LimpiadorAdapter;
import com.example.sparkv_v1.ADMIN.Clases.Limpiador;
import com.example.sparkv_v1.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeleccionarLimpiadorActivity extends AppCompatActivity {
    private RecyclerView recyclerViewLimpiadores;
    private LimpiadorAdapter adapter;
    private List<Limpiador> listaLimpiadores = new ArrayList<>();
    private String pedidoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_limpiador);

        recyclerViewLimpiadores = findViewById(R.id.recyclerViewLimpiadores);

        if (recyclerViewLimpiadores == null) {
            Log.e("Admin", "RecyclerView no encontrado");
            finish();
            return;
        }

        pedidoId = getIntent().getStringExtra("pedidoId");

        if (pedidoId == null) {
            Toast.makeText(this, "ID del pedido no proporcionado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewLimpiadores.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LimpiadorAdapter(listaLimpiadores, selectedCleanerId -> {
            FirebaseFirestore.getInstance().collection("pedidos_finalizados")
                    .document(pedidoId)
                    .update("idLimpiador", selectedCleanerId)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Limpiador asignado correctamente", Toast.LENGTH_SHORT).show();

                        Map<String, Object> asignacion = new HashMap<>();
                        asignacion.put("idPedido", pedidoId);
                        asignacion.put("idLimpiador", selectedCleanerId);
                        asignacion.put("timestamp", System.currentTimeMillis());
                        FirebaseFirestore.getInstance().collection("pedidos_asignados").add(asignacion);

                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al asignar limpiador: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        recyclerViewLimpiadores.setAdapter(adapter);

        cargarLimpiadores();
    }

    private void cargarLimpiadores() {
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("role", "limpiador")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d("Admin", "Número de limpiadores encontrados: " + querySnapshot.size());

                    listaLimpiadores.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String email = document.getString("email");
                        String userId = document.getId();

                        if (email != null && userId != null) {
                            listaLimpiadores.add(new Limpiador(userId, email));
                        } else {
                            Log.e("Admin", "Campos faltantes: " + document.getData().toString());
                        }
                    }

                    Log.d("Admin", "Lista de limpiadores cargada: " + listaLimpiadores.toString());
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Admin", "Error al cargar limpiadores: ", e);
                    Toast.makeText(this, "Error al cargar limpiadores: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}