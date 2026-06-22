package com.scooters_rent.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks", schema = "scooters")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scooter_id", nullable = false)
    private Scooter scooter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "juicer_id", nullable = false)
    private Juicer juicer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 1500)
    private String description;

    @Column(nullable = false)
    private boolean status;  // true = выполнена, false = не выполнена

    public Task() {}

    public Task(int id, Scooter scooter, Juicer juicer,
                LocalDateTime createdAt, String description, boolean status) {
        this.id = id;
        this.scooter = scooter;
        this.juicer = juicer;
        this.createdAt = createdAt;
        this.description = description;
        this.status = status;
    }

    public Task(Scooter scooter, Juicer juicer,
                LocalDateTime createdAt, String description, boolean status) {
        this(0, scooter, juicer, createdAt, description, status);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Scooter getScooter() { return scooter; }
    public void setScooter(Scooter scooter) { this.scooter = scooter; }

    public Juicer getJuicer() { return juicer; }
    public void setJuicer(Juicer juicer) { this.juicer = juicer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getStatusText() {
        return status ? "Выполнена" : "В работе";
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, scooter=%d, juicer='%s', status=%s, desc='%s'}",
                id,
                scooter.getId(),
                juicer.getFullName(),
                getStatusText(),
                description.length() > 50 ? description.substring(0, 47) + "..." : description);
    }
}