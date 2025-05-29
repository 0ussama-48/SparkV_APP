package com.example.sparkv_v1.CLIENTE.Clases;

public class Opcion {
    private String titulo;
    private int icono;

    public Opcion(String titulo, int icono) {
        this.titulo = titulo;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getIcono() {
        return icono;
    }
}