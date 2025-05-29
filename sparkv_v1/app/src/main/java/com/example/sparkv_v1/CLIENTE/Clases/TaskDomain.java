package com.example.sparkv_v1.CLIENTE.Clases;

import java.io.Serializable;

public class TaskDomain implements Serializable {
    private String title;
    private Double price;
    private String address;
    private String date;
    private String time;

    public TaskDomain(String title, Double price, String address, String date, String time) {
        this.title = title;
        this.price = price;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public String getTitle() { return title; }
    public Double getPrice() { return price; }
    public String getAddress() { return address; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}