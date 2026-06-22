package com.scooters_rent.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips", schema = "scooters")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scooter_id", nullable = false)
    private Scooter scooter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private int duration;  // в секундах

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    public Trip() {
    }

    public Trip(int id, Scooter scooter, User user, Route route,
                LocalDateTime startTime, int duration, BigDecimal cost) {
        this.id = id;
        this.scooter = scooter;
        this.user = user;
        this.route = route;
        this.startTime = startTime;
        this.duration = duration;
        this.cost = cost;
    }

    public Trip(Scooter scooter, User user, Route route,
                LocalDateTime startTime, int duration, BigDecimal cost) {
        this(0, scooter, user, route, startTime, duration, cost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getDistance() {
        return route != null ? route.getDistance() : 0;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getDurationMinutes() {
        return duration / 60;
    }

    @Override
    public String toString() {
        return String.format("Trip{id=%d, user='%s', scooter=%d, route=%d, duration=%d min, cost=%s}",
                id,
                user.getFullName(),
                scooter.getId(),
                route.getId(),
                getDurationMinutes(),
                cost);
    }
}
