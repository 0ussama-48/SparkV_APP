package com.example.sparkv_v1.CLIENTE.Clases;

public class Cliente {
    private String nombre;
    private String correo;
    private int numeroPedidos;
    private float calificacionPromedio;

    public Cliente(String nombre, String correo, int numeroPedidos, float calificacionPromedio) {
        this.nombre = nombre;
        this.correo = correo;
        this.numeroPedidos = numeroPedidos;
        this.calificacionPromedio = calificacionPromedio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getNumeroPedidos() {
        return numeroPedidos;
    }

    public void setNumeroPedidos(int numeroPedidos) {
        this.numeroPedidos = numeroPedidos;
    }

    public float getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(float calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
// Getters y Setters
}
