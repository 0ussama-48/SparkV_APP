package com.example.sparkv_v1.LIMPIADOR.Actividades;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.LIMPIADOR.Clases.Pedido;
import com.example.sparkv_v1.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallePedidoActivity extends AppCompatActivity {
    private TextView tvUsuario, tvTotal, tvItems, tvFecha, tvHora, tvDireccion;
    private Button btnFinalizar;
    private Pedido pedido;

    public static final String PEDIDO_EXTRA = "pedido";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        // Inicializar vistas
        tvUsuario = findViewById(R.id.tvUsuario);
        tvTotal = findViewById(R.id.tvTotal);
        tvItems = findViewById(R.id.tvItems);
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvDireccion = findViewById(R.id.tvDireccion);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        pedido = (Pedido) getIntent().getSerializableExtra(PEDIDO_EXTRA);

        if (pedido == null) {
            Toast.makeText(this, "No se recibió información del pedido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrarDetalles(pedido);

        btnFinalizar.setOnClickListener(v -> {
            if (pedido != null) {
                guardarEnHistorial(pedido);
                eliminarDeAsignados(pedido.getId());
                eliminarDeFinalizados(pedido.getId());
            }
        });
    }

    private void mostrarDetalles(Pedido pedido) {
        tvUsuario.setText("ID Usuario: " + pedido.getIdUsuario());
        tvTotal.setText("Total: €" + pedido.getTotal());
        tvFecha.setText("Fecha: " + pedido.getFecha());
        tvHora.setText("Hora: " + pedido.getHora());
        tvDireccion.setText("Dirección: " + pedido.getDireccion());

        StringBuilder itemsText = new StringBuilder();
        for (Pedido.Item item : pedido.getItems()) {
            itemsText.append("- ").append(item.getNombre()).append(": €").append(item.getPrecio()).append("\n");
        }
        tvItems.setText(itemsText.toString());
    }

    private void guardarEnHistorial(Pedido pedido) {
        FirebaseFirestore.getInstance().collection("historial_pedidos").add(pedido)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Pedido guardado en historial", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar en historial: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Admin", "Error al guardar en historial: ", e);
                });
    }

    private void eliminarDeAsignados(String idPedido) {
        FirebaseFirestore.getInstance().collection("pedidos_asignados")
                .whereEqualTo("idPedido", idPedido)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        finish(); // Volver a MisTareasActivity
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al eliminar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void eliminarDeFinalizados(String idPedido) {
        FirebaseFirestore.getInstance().collection("pedidos_finalizados")
                .document(idPedido)
                .delete()
                .addOnSuccessListener(aVoid -> {

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar de pedidos_finalizados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Admin", "Error al eliminar de pedidos_finalizados: ", e);
                });
    }
}