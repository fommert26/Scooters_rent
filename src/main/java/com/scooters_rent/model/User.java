package com.scooters_rent.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "scooters")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false, length = 500)
    private String fullName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trip> trips = new ArrayList<>();

    public User() {}

    public User(int id, String fullName, Tariff tariff) {
        this.id = id;
        this.fullName = fullName;
        this.tariff = tariff;
    }

    public User(String fullName, Tariff tariff) {
        this(0, fullName, tariff);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Tariff getTariff() { return tariff; }
    public void setTariff(Tariff tariff) { this.tariff = tariff; }

    public List<Trip> getTrips() { return trips; }
    public void setTrips(List<Trip> trips) { this.trips = trips; }

    public void addTrip(Trip trip) {
        trips.add(trip);
        trip.setUser(this);
    }

    public void removeTrip(Trip trip) {
        trips.remove(trip);
        trip.setUser(null);
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', tariff=%s}",
                id, fullName, tariff != null ? tariff.getName() : "none");
    }
}