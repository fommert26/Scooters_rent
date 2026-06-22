package com.scooters_rent.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "models", schema = "scooters")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 150)
    private String model;

    @Column(name = "generation")
    private Integer generation;  // может быть NULL в БД

    @Column(name = "model_coef", nullable = false, precision = 3, scale = 2)
    private BigDecimal modelCoef;  // NUMERIC(3,2) → BigDecimal

    public Model() {}

    public Model(int id, String brand, String model, Integer generation, BigDecimal modelCoef) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.modelCoef = modelCoef;
    }

    public Model(String brand, String model, Integer generation, BigDecimal modelCoef) {
        this(0, brand, model, generation, modelCoef);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getGeneration() { return generation; }
    public void setGeneration(Integer generation) { this.generation = generation; }

    public BigDecimal getModelCoef() { return modelCoef; }
    public void setModelCoef(BigDecimal modelCoef) { this.modelCoef = modelCoef; }

    @Override
    public String toString() {
        return String.format("Model{id=%d, %s %s, gen=%d, coef=%s}",
                id, brand, model, generation != null ? generation : 0, modelCoef);
    }
}
