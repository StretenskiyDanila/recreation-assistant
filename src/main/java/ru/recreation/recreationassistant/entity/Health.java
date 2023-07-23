package ru.recreation.recreationassistant.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "w_health")
public class Health {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "health_label")
    private String healthLabel;

    public Health() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHealthLabel() {
        return healthLabel;
    }

    public void setHealthLabel(String healthLabel) {
        this.healthLabel = healthLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Health health = (Health) o;
        return Objects.equals(id, health.id) && Objects.equals(healthLabel, health.healthLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, healthLabel);
    }

    @Override
    public String toString() {
        return "Health{" +
                "id=" + id +
                ", healthLabel='" + healthLabel + '\'' +
                '}';
    }
}
