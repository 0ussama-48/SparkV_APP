package com.example.sparkv_v1.ADMIN.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.CLIENTE.Clases.Pedido;
import com.example.sparkv_v1.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarPedidoActivity extends AppCompatActivity {

    private EditText edtCliente, edtServicio, edtPrecio;
    private Button btnSave;
    private FirebaseFirestore db;
    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        db = FirebaseFirestore.getInstance();
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");

        edtCliente = findViewById(R.id.edtCliente);
        edtServicio = findViewById(R.id.edtServicio);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnSave = findViewById(R.id.btnSave);

        if (pedido != null) {
            edtCliente.setText(pedido.getIdCliente());
            edtServicio.setText(pedido.getIdServicio());
            edtPrecio.setText((int) pedido.getTotal());
        }

        btnSave.setOnClickListener(v -> {
            String nuevoCliente = edtCliente.getText().toString().trim();
            String nuevoServicio = edtServicio.getText().toString().trim();
            Double nuevoPrecio = Double.parseDouble(edtPrecio.getText().toString());

            if (pedido != null) {
                db.collection("orders").document(pedido.getId())
                        .update(
                                "cliente", nuevoCliente,
                                "servicio", nuevoServicio,
                                "precio", nuevoPrecio
                        )
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Pedido actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}