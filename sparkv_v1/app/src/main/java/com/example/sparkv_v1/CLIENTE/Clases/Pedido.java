package com.example.sparkv_v1.CLIENTE.Clases;

import java.io.Serializable;

public class Pedido implements Serializable {
    private String id;
    private String idCliente;
    private String idServicio;
    private double total;
    private String metodoPago;
    private String estado;

    public Pedido() {}

    public Pedido(String id, String idCliente, String idServicio, double total, String metodoPago, String estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idServicio = idServicio;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estado = estado;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}