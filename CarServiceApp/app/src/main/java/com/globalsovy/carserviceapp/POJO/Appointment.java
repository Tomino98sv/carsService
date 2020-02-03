package com.globalsovy.carserviceapp.POJO;

import java.util.ArrayList;

public class Appointment {

    int id;
    String date;
    String time;
    String message;
    String brand;
    String model;
    ArrayList<String> urlImages;
    boolean expandV;

    public Appointment(int id, String date, String time, String message, String brand, String model) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.message = message;
        this.brand = brand;
        this.model = model;
        urlImages = new ArrayList<>();
        expandV = false;
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

    public boolean isExpandV() {
        return expandV;
    }

    public void setExpandV(boolean expandV) {
        this.expandV = expandV;
    }

    public ArrayList<String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(ArrayList<String> urlImages) {
        this.urlImages = urlImages;
    }
}
