package com.example.sparkv_v1.CLIENTE.Clases;

public class CategoriaDomain {
    private String titulo;
    private String pic;

    public CategoriaDomain(String titulo, String pic) {
        this.titulo = titulo;
        this.pic = pic;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
