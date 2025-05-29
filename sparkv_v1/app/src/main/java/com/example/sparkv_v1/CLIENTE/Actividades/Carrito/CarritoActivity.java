package com.example.sparkv_v1.CLIENTE.Actividades.Carrito;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sparkv_v1.CLIENTE.Actividades.Home.ClienteMainActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.MisReservasActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.PerfilActivity;
import com.example.sparkv_v1.CLIENTE.Actividades.Soporte.SoporteActivity;
import com.example.sparkv_v1.CLIENTE.Adaptadores.CarritoAdapter;
import com.example.sparkv_v1.CLIENTE.Clases.CarritoItem;
import com.example.sparkv_v1.CLIENTE.Actividades.Perfil.DireccionesActivity;
import com.example.sparkv_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CarritoActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_ADDRESS = 1;
    private RecyclerView recyclerViewCarrito;
    private Button btnFinalizar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<CarritoItem> carritoItems;
    private CarritoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        carritoItems = new ArrayList<>();
        adapter = new CarritoAdapter(carritoItems, this::eliminarItem);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCarrito.setAdapter(adapter);

        cargarItemsCarrito();

        btnFinalizar.setOnClickListener(v -> {
            if (carritoItems.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Abrir DireccionesActivity para seleccionar dirección
            Intent intent = new Intent(CarritoActivity.this, DireccionesActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_ADDRESS);
        });

        // Botón de Inicio
        View inicioBtn = findViewById(R.id.inicioBtn);
        if (inicioBtn != null) {
            inicioBtn.setOnClickListener(view -> {
                Intent intent = new Intent(CarritoActivity.this, ClienteMainActivity.class);
                startActivity(intent);
            });
        }

        // Botón de Perfil
        View perfilBtn = findViewById(R.id.PerfilBtn);
        if (perfilBtn != null) {
            perfilBtn.setOnClickListener(view -> {
                Intent intent = new Intent(CarritoActivity.this, PerfilActivity.class);
                startActivity(intent);
            });
        }

        // Botón de Soporte
        View soporteBtn = findViewById(R.id.soporteBtn);
        if (soporteBtn != null) {
            soporteBtn.setOnClickListener(view -> {
                Intent intent = new Intent(CarritoActivity.this, SoporteActivity.class);
                startActivity(intent);
            });
        }

        // Botón de Reservas
        View reservasBtn = findViewById(R.id.reservasBtn);
        if (reservasBtn != null) {
            reservasBtn.setOnClickListener(v -> {
                Intent intent = new Intent(CarritoActivity.this, MisReservasActivity.class);
                startActivity(intent);
            });
        }
    }

    private void cargarItemsCarrito() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId).collection("carrito")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    carritoItems.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        CarritoItem item = document.toObject(CarritoItem.class);
                        if (item != null) {
                            carritoItems.add(item);
                        } else {
                            Log.e("Carrito", "No se pudo mapear el objeto CarritoItem");
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar carrito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Carrito", "Error al cargar carrito: ", e);
                });
    }

    private void eliminarItem(String servicioId) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(currentUser.getUid()).collection("carrito")
                .whereEqualTo("servicioId", servicioId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        document.getReference().delete().addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Servicio eliminado", Toast.LENGTH_SHORT).show();
                            cargarItemsCarrito();
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar servicio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Carrito", "Error al eliminar servicio: ", e);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_ADDRESS && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String calle = data.getStringExtra("selected_calle");
                String numero = data.getStringExtra("selected_numero");
                String ciudad = data.getStringExtra("selected_ciudad");
                String codigoPostal = data.getStringExtra("selected_codigo_postal");

                String direccionCompleta = calle + ", " + numero + ", " + ciudad + ", " + codigoPostal;
                guardarPedido(direccionCompleta);
            } else {
                Toast.makeText(this, "No se seleccionó una dirección", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarPedido(String direccionSeleccionada) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0;
        for (CarritoItem item : carritoItems) {
            total += item.getPrecio() * item.getCantidad();
        }

        Map<String, Object> pedidoData = new HashMap<>();
        pedidoData.put("usuarioId", currentUser.getUid());
        pedidoData.put("direccion", direccionSeleccionada);
        pedidoData.put("items", carritoItems); // Firestore serializa automáticamente si implementa Serializable
        pedidoData.put("total", total);
        pedidoData.put("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        db.collection("pedidos_finalizados").add(pedidoData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Pedido guardado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCarrito();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Carrito", "Error al guardar pedido: ", e);
                });
    }

    private void limpiarCarrito() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        db.collection("users").document(currentUser.getUid()).collection("carrito").get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        document.getReference().delete();
                    }
                    carritoItems.clear();
                    adapter.notifyDataSetChanged();
                    finish();
                });
    }
}