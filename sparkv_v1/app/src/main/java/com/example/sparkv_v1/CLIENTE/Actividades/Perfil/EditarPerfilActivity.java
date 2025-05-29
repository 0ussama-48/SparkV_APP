package com.example.sparkv_v1.CLIENTE.Actividades.Perfil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, bioInput;
    private Button saveButton;
    private ImageView profilePicture;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ImageView volverBtn = findViewById(R.id.volverBtn);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        bioInput = findViewById(R.id.bioInput);
        saveButton = findViewById(R.id.saveButton);

        // Obtener datos del perfil desde Firestore
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String username = task.getResult().getString("name");
                String email = task.getResult().getString("email");
                String bio = task.getResult().getString("bio");

                usernameInput.setText(username);
                emailInput.setText(email);
                bioInput.setText(bio);
            }
        });

        // Acción del botón "Volver"
        volverBtn.setOnClickListener(v -> finish());

        // Acción del botón "Guardar Cambios"
        saveButton.setOnClickListener(v -> {
            String newUsername = usernameInput.getText().toString().trim();
            String newEmail = emailInput.getText().toString().trim();
            String newBio = bioInput.getText().toString().trim();

            if (newUsername.isEmpty()) {
                usernameInput.setError("El nombre es obligatorio");
                return;
            }
            if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                emailInput.setError("Ingresa un correo electrónico válido");
                return;
            }

            // Actualizar datos en Firestore
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", newUsername);
            updates.put("email", newEmail);
            updates.put("bio", newBio);

            userRef.update(updates).addOnCompleteListener(updateTask -> {
                if (updateTask.isSuccessful()) {
                    Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}