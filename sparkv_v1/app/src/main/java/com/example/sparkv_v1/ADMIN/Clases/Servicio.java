package com.example.sparkv_v1.ADMIN.Clases;

import java.io.Serializable;

public class Servicio implements Serializable {
    private String id;
    private String nombre;
    private String descripcion;
    private Double precio;

    public Servicio(String id, String nombre, String descripcion, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
}