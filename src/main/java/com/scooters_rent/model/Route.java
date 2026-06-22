package com.scooters_rent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "routes", schema = "scooters")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "start_zone_id", nullable = false)
    private Zone startZone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_zone_id", nullable = false)
    private Zone endZone;

    @Column(nullable = false)
    private short distance;  //SMALLINT, от 1 до 99 км

    @Column(name = "speed_limit")
    private Short speedLimit;  //от 10 до 59 км/ч

    public Route() {}

    public Route(int id, Zone startZone, Zone endZone, short distance, Short speedLimit) {
        this.id = id;
        this.startZone = startZone;
        this.endZone = endZone;
        this.distance = distance;
        this.speedLimit = speedLimit;
    }

    public Route(Zone startZone, Zone endZone, short distance, Short speedLimit) {
        this(0, startZone, endZone, distance, speedLimit);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Zone getStartZone() { return startZone; }
    public void setStartZone(Zone startZone) { this.startZone = startZone; }

    public Zone getEndZone() { return endZone; }
    public void setEndZone(Zone endZone) { this.endZone = endZone; }

    public short getDistance() { return distance; }
    public void setDistance(short distance) { this.distance = distance; }

    public Short getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(Short speedLimit) { this.speedLimit = speedLimit; }

    @Override
    public String toString() {
        return String.format("Route{id=%d, from='%s', to='%s', distance=%dkm, speedLimit=%s}",
                id,
                startZone != null ? startZone.getAddress() : "?",
                endZone != null ? endZone.getAddress() : "?",
                distance,
                speedLimit != null ? speedLimit + " km/h" : "no limit");
    }
}