package com.example.dibapp.module;

public final class Position {
    public double accuracy;
    public double latitude;
    public double longitude;

    public Position() {

    }

    public Position(double accuracy, double latitude, double longitude) {
        this.accuracy = accuracy;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int distanceTo(Position position){
        return (int) (Math.sqrt((Math.pow(this.latitude-position.getLatitude(),2)+Math.pow(this.longitude-position.getLongitude(),2)))*(100000));
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}