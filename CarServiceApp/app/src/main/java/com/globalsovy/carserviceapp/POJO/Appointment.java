package com.globalsovy.carserviceapp.POJO;

public class Appointment {

    int id;
    String date;
    String time;
    String message;
    String brand;
    String model;

    public Appointment(int id, String date, String time, String message, String brand, String model) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.message = message;
        this.brand = brand;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
