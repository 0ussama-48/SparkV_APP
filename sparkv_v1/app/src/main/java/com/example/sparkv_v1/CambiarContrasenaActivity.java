package com.example.sparkv_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CambiarContrasenaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText contrasenaActualInput, nuevaContrasenaInput;
    private Button botonConfirmarCambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        contrasenaActualInput = findViewById(R.id.contrasenaActualInput);
        nuevaContrasenaInput = findViewById(R.id.nuevaContrasenaInput);
        botonConfirmarCambio = findViewById(R.id.botonConfirmarCambio);

        botonConfirmarCambio.setOnClickListener(v -> {
            String contrasenaActual = contrasenaActualInput.getText().toString().trim();
            String nuevaContrasena = nuevaContrasenaInput.getText().toString().trim();

            if (contrasenaActual.isEmpty() || nuevaContrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nuevaContrasena.length() < 6) {
                Toast.makeText(this, "La nueva contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            cambiarContrasena(contrasenaActual, nuevaContrasena);
        });
    }

    private void cambiarContrasena(String contrasenaActual, String nuevaContrasena) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail();

            // Reautenticar al usuario con la contraseña actual
            mAuth.signInWithEmailAndPassword(email, contrasenaActual)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Actualizar la contraseña en Firebase Authentication
                            user.updatePassword(nuevaContrasena)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Contraseña actualizada correctamente
                                            Toast.makeText(CambiarContrasenaActivity.this,
                                                    "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();

                                            // Redirigir al usuario a la pantalla principal
                                            Intent intent = new Intent(CambiarContrasenaActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Error al actualizar la contraseña
                                            Toast.makeText(CambiarContrasenaActivity.this,
                                                    "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Contraseña actual incorrecta
                            Toast.makeText(CambiarContrasenaActivity.this,
                                    "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}