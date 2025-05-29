package com.example.sparkv_v1.ADMIN.Clases;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String id;
    private String nombre;
    private String correo;
    private String rol;

    public Usuario(String id, String nombre, String correo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
}