package com.prime.busstopalert.model;

/**
 * Created by SantaClaus on 22/02/2017.
 */

public class BusStop {
    private String name;
    private double lat;
    private double log;

    public BusStop(String name, double lat, double log) {
        this.name = name;
        this.lat = lat;
        this.log = log;
    }

    public BusStop() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }
}
