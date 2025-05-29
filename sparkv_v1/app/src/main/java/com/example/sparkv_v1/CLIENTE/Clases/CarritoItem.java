package com.example.sparkv_v1.CLIENTE.Clases;

import java.io.Serializable;

public class CarritoItem implements Serializable {
    private String nombre, categoria, duracion, detalles, fecha, hora, servicioId;
    private double precio;
    private int cantidad;

    public CarritoItem() {}

    public CarritoItem(String nombre, double precio, int cantidad, String categoria, String duracion, String detalles, String fecha, String hora, String servicioId) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.duracion = duracion;
        this.detalles = detalles;
        this.fecha = fecha;
        this.hora = hora;
        this.servicioId = servicioId;
    }

    // Getters públicos para Firestore
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public String getCategoria() { return categoria; }
    public String getDuracion() { return duracion; }
    public String getDetalles() { return detalles; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getServicioId() { return servicioId; }
}