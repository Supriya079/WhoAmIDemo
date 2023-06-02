package com.supriya.whoami.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String placeName;
    private double latitude;
    private double longitude;

    // Getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Constructor
    public LocationEntity(String placeName, double latitude, double longitude) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
