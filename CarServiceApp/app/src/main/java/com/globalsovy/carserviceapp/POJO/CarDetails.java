package com.globalsovy.carserviceapp.POJO;

public class CarDetails {
    int idDetails;
    int userId;
    int carId;
    String SPZ;
    String color;
    int vintage;
    int kilometrage;
    int profileImgID;
    String fuel;
    boolean manualtrans;
    double volume;
    String brand;
    String model;
    String docPath;

    public CarDetails(int idDetails, int userId, int carId, String SPZ, String color, int vintage, int kilometrage, int profileImgID, String fuel, boolean manualtrans, double volume, String brand, String model, String docPath) {
        this.idDetails = idDetails;
        this.userId = userId;
        this.carId = carId;
        this.SPZ = SPZ;
        this.color = color;
        this.vintage = vintage;
        this.kilometrage = kilometrage;
        this.profileImgID = profileImgID;
        this.fuel = fuel;
        this.manualtrans = manualtrans;
        this.volume = volume;
        this.brand = brand;
        this.model = model;
        this.docPath = docPath;
    }

    public int getIdDetails() {
        return idDetails;
    }

    public void setIdDetails(int idDetails) {
        this.idDetails = idDetails;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getSPZ() {
        return SPZ;
    }

    public void setSPZ(String SPZ) {
        this.SPZ = SPZ;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    public int getProfileImgID() {
        return profileImgID;
    }

    public void setProfileImgID(int profileImgID) {
        this.profileImgID = profileImgID;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public boolean isManualtrans() {
        return manualtrans;
    }

    public void setManualtrans(boolean manualtrans) {
        this.manualtrans = manualtrans;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
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

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }
}
