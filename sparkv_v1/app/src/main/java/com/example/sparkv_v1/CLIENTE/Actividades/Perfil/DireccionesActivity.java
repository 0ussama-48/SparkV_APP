package com.example.sparkv_v1.CLIENTE.Actividades.Perfil;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Clases.Direccion;
import com.example.sparkv_v1.CLIENTE.Adaptadores.DireccionesAdaptador;
import com.example.sparkv_v1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DireccionesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDirecciones;
    private FloatingActionButton addAddressButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Direccion> direccionesList;
    private DireccionesAdaptador adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direcciones);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViewDirecciones = findViewById(R.id.recyclerViewDirecciones);
        addAddressButton = findViewById(R.id.addAddressButton);

        direccionesList = new ArrayList<>();
        adapter = new DireccionesAdaptador(direccionesList, this);
        recyclerViewDirecciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDirecciones.setAdapter(adapter);

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("direcciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        direccionesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Direccion direccion = document.toObject(Direccion.class);
                            direccionesList.add(direccion);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(DireccionesActivity.this, AgregarDireccionActivity.class);
            startActivity(intent);
        });
    }
}