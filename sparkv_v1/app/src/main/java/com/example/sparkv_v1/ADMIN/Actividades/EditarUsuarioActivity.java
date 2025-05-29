package com.example.sparkv_v1.ADMIN.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sparkv_v1.ADMIN.Clases.Usuario;
import com.example.sparkv_v1.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarUsuarioActivity extends AppCompatActivity {

    private EditText edtNombre, edtCorreo, edtRol;
    private Button btnEliminar, btnSave;
    private FirebaseFirestore db;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        db = FirebaseFirestore.getInstance();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        edtNombre = findViewById(R.id.edtNombre);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtRol = findViewById(R.id.edtRol);
        btnSave = findViewById(R.id.btnSave);
        btnEliminar = findViewById(R.id.btnEliminar);

        if (usuario != null) {
            edtNombre.setText(usuario.getNombre());
            edtCorreo.setText(usuario.getCorreo());
            edtRol.setText(usuario.getRol());
        }

        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Usuario")
                    .setMessage("¿Estás seguro de eliminar este usuario?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        db.collection("users").document(usuario.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
}