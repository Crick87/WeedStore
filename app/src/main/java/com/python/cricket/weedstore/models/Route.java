package com.python.cricket.weedstore.models;

public class Route {
    private int idPath;
    private Latlong latLong;
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getIdPath() {
        return idPath;
    }

    public void setIdPath(int idPath) {
        this.idPath = idPath;
    }

    public Latlong getLatLong() {
        return latLong;
    }

    public void setLatLong(Latlong latLong) {
        this.latLong = latLong;
    }
}
