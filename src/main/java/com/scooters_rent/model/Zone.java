package com.scooters_rent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "zones", schema = "scooters")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "parking_available", nullable = false)
    private boolean parkingAvailable;

    public Zone() {}

    public Zone(int id, String address, boolean parkingAvailable) {
        this.id = id;
        this.address = address;
        this.parkingAvailable = parkingAvailable;
    }

    public Zone(String address, boolean parkingAvailable) {
        this(0, address, parkingAvailable);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isParkingAvailable() { return parkingAvailable; }
    public void setParkingAvailable(boolean parkingAvailable) { this.parkingAvailable = parkingAvailable; }

    @Override
    public String toString() {
        return String.format("Zone{id=%d, address='%s', parkingAvailable=%s}",
                id, address, parkingAvailable);
    }
}