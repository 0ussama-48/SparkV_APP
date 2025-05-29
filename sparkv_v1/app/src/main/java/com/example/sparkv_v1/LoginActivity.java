package com.example.sparkv_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.ADMIN.Actividades.PanelControlActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Home.ClienteMainActivity;
import com.example.sparkv_v1.LIMPIADOR.Actividades.LimpiadorMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView registerLink = findViewById(R.id.registerLink);

        // Botón de inicio de sesión
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();

                                db.collection("users").document(userId).get()
                                        .addOnCompleteListener(snapshotTask -> {
                                            if (snapshotTask.isSuccessful()) {
                                                DocumentSnapshot snapshot = snapshotTask.getResult();
                                                if (snapshot != null && snapshot.exists()) {
                                                    String role = snapshot.getString("role");

                                                    Intent intent;
                                                    if ("cliente".equalsIgnoreCase(role)) {
                                                        intent = new Intent(LoginActivity.this, ClienteMainActivity.class);
                                                    } else if ("limpiador".equalsIgnoreCase(role)) {
                                                        intent = new Intent(LoginActivity.this, LimpiadorMainActivity.class);
                                                    } else if ("administrador".equalsIgnoreCase(role)) {
                                                        intent = new Intent(LoginActivity.this, PanelControlActivity.class);
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Usuario no encontrado en la base de datos", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Inicio de sesión fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Restablecer contraseña
        forgotPassword.setOnClickListener(v -> {
            EditText resetEmail = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Restablecer contraseña")
                    .setMessage("Ingresa tu correo electrónico:")
                    .setView(resetEmail)
                    .setPositiveButton("Enviar", (dialog, which) -> {
                        String email = resetEmail.getText().toString().trim();
                        if (!email.isEmpty()) {
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(this, "Correo enviado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Por favor, ingresa un correo electrónico", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}