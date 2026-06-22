package com.scooters_rent.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tariffs", schema = "scooters")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "price_per_minute", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerMinute;

    public Tariff() {}

    public Tariff(int id, String name, BigDecimal pricePerMinute) {
        this.id = id;
        this.name = name;
        setPricePerMinute(pricePerMinute);
    }

    public Tariff(String name, BigDecimal pricePerMinute) {
        this(0, name, pricePerMinute);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPricePerMinute() { return pricePerMinute; }

    public void setPricePerMinute(BigDecimal pricePerMinute) {
        if (pricePerMinute == null) {
            throw new IllegalArgumentException("Цена не может быть null");
        }
        if (pricePerMinute.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
        if (pricePerMinute.compareTo(new BigDecimal("10.00")) >= 0) {
            throw new IllegalArgumentException("Цена должна быть меньше 10");
        }
        this.pricePerMinute = pricePerMinute;
    }

    @Override
    public String toString() {
        return String.format("Tariff{id=%d, name='%s', pricePerMinute=%s}",
                id, name, pricePerMinute);
    }
}