package com.example.sparkv_v1.CLIENTE.Actividades.Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarDireccionActivity extends AppCompatActivity {

    private EditText calleInput, numeroCasaInput, ciudadInput, codigoPostalInput;
    private Button guardarButton, eliminarButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String direccionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_direccion);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        direccionId = intent.getStringExtra("id");

        if (direccionId == null || direccionId.isEmpty()) {
            Toast.makeText(this, "Error: ID de dirección no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String calle = intent.getStringExtra("calle");
        String numeroCasa = intent.getStringExtra("numeroCasa");
        String ciudad = intent.getStringExtra("ciudad");
        String codigoPostal = intent.getStringExtra("codigoPostal");

        calleInput = findViewById(R.id.calleInput);
        numeroCasaInput = findViewById(R.id.numeroCasaInput);
        ciudadInput = findViewById(R.id.ciudadInput);
        codigoPostalInput = findViewById(R.id.codigoPostalInput);
        guardarButton = findViewById(R.id.guardarButton);
        eliminarButton = findViewById(R.id.eliminarButton);

        calleInput.setText(calle);
        numeroCasaInput.setText(numeroCasa);
        ciudadInput.setText(ciudad);
        codigoPostalInput.setText(codigoPostal);

        // Acción del botón "Guardar"
        guardarButton.setOnClickListener(v -> {
            String nuevaCalle = calleInput.getText().toString().trim();
            String nuevoNumeroCasa = numeroCasaInput.getText().toString().trim();
            String nuevaCiudad = ciudadInput.getText().toString().trim();
            String nuevoCodigoPostal = codigoPostalInput.getText().toString().trim();

            if (nuevaCalle.isEmpty() || nuevoNumeroCasa.isEmpty() || nuevaCiudad.isEmpty() || nuevoCodigoPostal.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar la dirección en Firestore
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users").document(userId).collection("direcciones").document(direccionId)
                    .update(
                            "calle", nuevaCalle,
                            "numeroCasa", nuevoNumeroCasa,
                            "ciudad", nuevaCiudad,
                            "codigoPostal", nuevoCodigoPostal
                    )
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Dirección actualizada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al guardar la dirección", Toast.LENGTH_SHORT).show();
                    });
        });

        // Acción del botón "Eliminar"
        eliminarButton.setOnClickListener(v -> {
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users").document(userId).collection("direcciones").document(direccionId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Dirección eliminada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al eliminar la dirección", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}