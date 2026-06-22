package com.scooters_rent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "scooters", schema = "scooters")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Zone location;

    @Column(name = "charge_level", nullable = false)
    private short chargeLevel;  // от 0 до 100

    public Scooter() {}

    public Scooter(int id, Model model, Zone location, short chargeLevel) {
        this.id = id;
        this.model = model;
        this.location = location;
        this.chargeLevel = chargeLevel;
    }

    public Scooter(Model model, Zone location, short chargeLevel) {
        this(0, model, location, chargeLevel);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Model getModel() { return model; }
    public void setModel(Model model) { this.model = model; }

    public Zone getLocation() { return location; }
    public void setLocation(Zone location) { this.location = location; }

    public short getChargeLevel() { return chargeLevel; }
    public void setChargeLevel(short chargeLevel) { this.chargeLevel = chargeLevel; }

    public boolean isLowBattery() {
        return chargeLevel < 15;
    }

    @Override
    public String toString() {
        return String.format("Scooter{id=%d, model='%s %s', location='%s', charge=%d%%}",
                id, model.getBrand(), model.getModel(), location.getAddress(), chargeLevel);
    }
}
