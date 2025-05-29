package com.example.sparkv_v1.CLIENTE.Clases;

public class Direccion {
    private String id;
    private String calle;
    private String numeroCasa;
    private String ciudad;
    private String codigoPostal;

    public Direccion() {
    }

    public Direccion(String id, String calle, String numeroCasa, String ciudad, String codigoPostal) {
        this.id = id;
        this.calle = calle;
        this.numeroCasa = numeroCasa;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    public String getId() {
        return id;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }
}