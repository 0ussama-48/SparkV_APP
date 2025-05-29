package com.example.sparkv_v1.CLIENTE.Clases;

public class ReservaDomain {
    private String servicio;
    private String fecha;
    private String hora;
    private String estado;

    public ReservaDomain(String servicio, String fecha, String hora, String estado) {
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public String getServicio() { return servicio; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstado() { return estado; }
}