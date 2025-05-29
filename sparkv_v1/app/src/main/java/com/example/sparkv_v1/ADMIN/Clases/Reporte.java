package com.example.sparkv_v1.ADMIN.Clases;

import java.io.Serializable;

public class Reporte implements Serializable {
    private String id;
    private String descripcion;
    private String userId;
    private String userName;
    private String fecha;

    public Reporte(String id, String descripcion, String userId, String userName, String fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.userId = userId;
        this.userName = userName;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getFecha() { return fecha; }
}