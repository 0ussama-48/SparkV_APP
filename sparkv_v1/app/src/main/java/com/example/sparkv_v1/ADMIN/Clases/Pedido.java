package com.example.sparkv_v1.ADMIN.Clases;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    private String id;
    private String idUsuario;
    private Double total;
    private List<Item> items;
    private String idLimpiador;

    public static class Item implements Serializable {
        private String nombre;
        private Double precio;
        private String categoria;
        private String duracion;
        private String idServicio;

        public Item(String nombre, Double precio, String categoria, String duracion, String idServicio) {
            this.nombre = nombre;
            this.precio = precio;
            this.categoria = categoria;
            this.duracion = duracion;
            this.idServicio = idServicio;
        }

        public String getNombre() { return nombre; }
        public Double getPrecio() { return precio; }
        public String getCategoria() { return categoria; }
        public String getDuracion() { return duracion; }
        public String getIdServicio() { return idServicio; }
    }

    public Pedido(String id, String idUsuario, Double total, List<Item> items, String idLimpiador) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.total = total;
        this.items = items;
        this.idLimpiador = idLimpiador;
    }

    public String getId() { return id; }
    public String getIdUsuario() { return idUsuario; }
    public Double getTotal() { return total; }
    public List<Item> getItems() { return items; }
    public String getIdLimpiador() { return idLimpiador; }
    public void setIdLimpiador(String idLimpiador) { this.idLimpiador = idLimpiador; }
}