package com.example.sparkv_v1.LIMPIADOR.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {
    private String id;
    private String idUsuario;
    private String idLimpiador;
    private Double total;
    private String fecha;
    private String hora;
    private String direccion;
    private ArrayList<Item> items;

    public Pedido() {}

    public Pedido(String id, String idUsuario, Double total, ArrayList<Item> items, String idLimpiador) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.total = total;
        this.items = items;
        this.idLimpiador = idLimpiador;
    }

    public static class Item implements Serializable {
        private String nombre;
        private Double precio;
        private String categoria;
        private String duracion;
        private String servicioId;

        public Item() {}

        public Item(String nombre, Double precio, String categoria, String duracion, String servicioId) {
            this.nombre = nombre;
            this.precio = precio;
            this.categoria = categoria;
            this.duracion = duracion;
            this.servicioId = servicioId;
        }

        // Getters públicos necesarios para Firestore
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public String getDuracion() { return duracion; }
        public void setDuracion(String duracion) { this.duracion = duracion; }

        public String getServicioId() { return servicioId; }
        public void setServicioId(String servicioId) { this.servicioId = servicioId; }
    }

    // Getters públicos necesarios para Firestore
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getIdLimpiador() { return idLimpiador; }
    public void setIdLimpiador(String idLimpiador) { this.idLimpiador = idLimpiador; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public ArrayList<Item> getItems() { return items; }
    public void setItems(ArrayList<Item> items) { this.items = items; }
}