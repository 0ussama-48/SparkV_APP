package com.example.sparkv_v1.LIMPIADOR.Actividades;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.sparkv_v1.CLIENTE.Adaptadores.PerfilAdaptador;
import com.example.sparkv_v1.CLIENTE.Clases.Opcion;
import com.example.sparkv_v1.LoginActivity;
import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity {

    private TextView username, email, bio;
    private Button logoutButton;
    private ImageView profilePicture;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profile_pictures");

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        bio = findViewById(R.id.bio);
        logoutButton = findViewById(R.id.logoutButton);

        if (username == null || email == null || bio == null || logoutButton == null) {
            Log.e("PerfilActivity", "Error: Una o más vistas no están inicializadas correctamente");
            finish();
            return;
        }

        RecyclerView recyclerViewOptions = findViewById(R.id.recyclerViewOptions);
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Opcion> opciones = new ArrayList<>();
        opciones.add(new Opcion("Editar perfil", R.drawable.ic_edit_profile));
        opciones.add(new Opcion("Cambiar contraseña", R.drawable.ic_change_password));
        opciones.add(new Opcion("Historial", R.drawable.ic_my_bookings));
        opciones.add(new Opcion("Política de Privacidad", R.drawable.ic_privacy_policy));
        opciones.add(new Opcion("Términos y Condiciones", R.drawable.ic_terms_conditions));

        PerfilAdaptador adapter = new PerfilAdaptador(opciones, this);
        recyclerViewOptions.setAdapter(adapter);

        // Obtener datos del usuario desde Firestore
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String userEmail = document.getString("email");
                                String bioText = document.getString("bio");

                                username.setText(name != null ? name : "Usuario");
                                email.setText(userEmail != null ? userEmail : "Correo no disponible");
                                bio.setText(bioText != null ? bioText : "Biografía no disponible");

                            } else {
                                Toast.makeText(this, "El documento del usuario no existe", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Error al cargar datos del usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("PerfilActivity", "Error al obtener datos del usuario", task.getException());
                        }
                    });
        } else {
            startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
            finish();
        }

        // Configurar el botón de cerrar sesión
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Cerrar sesión
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


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
     * Acción al hacer clic en el botón "Ajustes".
     */
    public void onSettingsClick(View view){
        Intent intent = new Intent(this, HistorialActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Glide.with(this).load(selectedImageUri).into(profilePicture);

                    subirImagenPerfil(selectedImageUri);
                } catch (Exception e) {
                    Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    Log.e("PerfilActivity", "Error al cargar imagen seleccionada", e);
                }
            }
        }
    }

    private void subirImagenPerfil(Uri imageUri) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No estás autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        StorageReference fileReference = storageReference.child(userId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        db.collection("users").document(userId)
                                .update("profilePictureUrl", imageUrl)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al actualizar Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("PerfilActivity", "Firestore error: ", e);
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al obtener URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("PerfilActivity", "Storage URL error: ", e);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al subir imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("PerfilActivity", "Storage upload error: ", e);
                });
    }
}