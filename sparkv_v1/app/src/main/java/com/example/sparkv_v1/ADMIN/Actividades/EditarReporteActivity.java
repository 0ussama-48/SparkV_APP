package com.example.sparkv_v1.ADMIN.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.ADMIN.Clases.Reporte;
import com.example.sparkv_v1.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarReporteActivity extends AppCompatActivity {

    private EditText edtDescripcion;
    private Button btnSave;
    private FirebaseFirestore db;
    private Reporte reporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_reporte);

        db = FirebaseFirestore.getInstance();
        reporte = (Reporte) getIntent().getSerializableExtra("reporte");

        edtDescripcion = findViewById(R.id.edtDescripcion);
        btnSave = findViewById(R.id.btnSave);

        if (reporte != null) {
            edtDescripcion.setText(reporte.getDescripcion());
        }

        btnSave.setOnClickListener(v -> {
            String nuevaDescripcion = edtDescripcion.getText().toString().trim();

            if (reporte != null && !nuevaDescripcion.isEmpty()) {
                db.collection("reportes_problemas").document(reporte.getId())
                        .update(
                                "descripcion", nuevaDescripcion
                        )
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Reporte actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
            }
        });
    }
}