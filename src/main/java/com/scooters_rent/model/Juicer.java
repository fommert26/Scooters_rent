package com.scooters_rent.model;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "juicers", schema = "scooters")
public class Juicer {

    private static final List<String> VALID_ROLES = Arrays.asList(
            "repairman", "constellator", "administrator"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false, length = 500)
    private String fullName;

    @Column(length = 50)
    private String role;

    public Juicer() {}

    public Juicer(int id, String fullName, String role) {
        this.id = id;
        this.fullName = fullName;
        setRole(role);
    }

    public Juicer(String fullName, String role) {
        this(0, fullName, role);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }

    public void setRole(String role) {
        if (role == null || role.isEmpty() || VALID_ROLES.contains(role)) {
            this.role = role;
        } else {
            this.role = null;
        }
    }

    @Override
    public String toString() {
        return String.format("Juicer{id=%d, name='%s', role=%s}", id, fullName, role != null ? role : "none");
    }
}