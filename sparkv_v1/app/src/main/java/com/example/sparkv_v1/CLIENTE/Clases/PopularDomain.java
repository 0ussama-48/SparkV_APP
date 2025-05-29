package com.example.sparkv_v1.CLIENTE.Clases;

import java.io.Serializable;

public class PopularDomain implements Serializable {
    private String title;
    private String pic;
    private String descripcion;
    private Double fee;
    private int numberInCart;
    private String categoria;
    private String duracion;
    private String detallesAdicionales;
    private String servicioId;

    public PopularDomain(String title, String pic, String descripcion, Double fee, int numberInCart, String categoria, String duracion, String detallesAdicionales, String servicioId) {
        this.title = title;
        this.pic = pic;
        this.descripcion = descripcion;
        this.fee = fee;
        this.numberInCart = numberInCart;
        this.categoria = categoria;
        this.duracion = duracion;
        this.detallesAdicionales = detallesAdicionales;
        this.servicioId = servicioId;
    }

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getFee() { return fee; }
    public void setFee(Double fee) { this.fee = fee; }

    public int getNumberInCart() { return numberInCart; }
    public void setNumberInCart(int numberInCart) { this.numberInCart = numberInCart; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getDetallesAdicionales() { return detallesAdicionales; }
    public void setDetallesAdicionales(String detallesAdicionales) { this.detallesAdicionales = detallesAdicionales; }

    public String getServicioId() { return servicioId; }
    public void setServicioId(String servicioId) { this.servicioId = servicioId; }
}