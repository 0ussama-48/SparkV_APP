package com.example.sparkv_v1.ADMIN.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.sparkv_v1.ADMIN.Clases.Servicio;
import com.example.sparkv_v1.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarServicioActivity extends AppCompatActivity {

    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnSave;
    private FirebaseFirestore db;
    private Servicio servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servicio);

        db = FirebaseFirestore.getInstance();
        servicio = (Servicio) getIntent().getSerializableExtra("servicio");

        edtNombre = findViewById(R.id.edtNombre);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnSave = findViewById(R.id.btnSave);

        if (servicio != null) {
            edtNombre.setText(servicio.getNombre());
            edtDescripcion.setText(servicio.getDescripcion());
            edtPrecio.setText(servicio.getPrecio().toString());
        }

        btnSave.setOnClickListener(v -> {
            String nuevoNombre = edtNombre.getText().toString().trim();
            String nuevaDescripcion = edtDescripcion.getText().toString().trim();
            Double nuevoPrecio = Double.parseDouble(edtPrecio.getText().toString());

            if (servicio != null) {
                db.collection("servicios").document(servicio.getId())
                        .update(
                                "nombre", nuevoNombre,
                                "descripcion", nuevaDescripcion,
                                "precio", nuevoPrecio
                        )
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Servicio actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}