package ru.recreation.recreationassistant.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "w_cuisine")
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuisine_label")
    private String cuisineLabel;

    public Cuisine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCuisineLabel() {
        return cuisineLabel;
    }

    public void setCuisineLabel(String cuisineLabel) {
        this.cuisineLabel = cuisineLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuisine cuisine = (Cuisine) o;
        return Objects.equals(id, cuisine.id) && Objects.equals(cuisineLabel, cuisine.cuisineLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cuisineLabel);
    }

    @Override
    public String toString() {
        return "Cuisine{" +
                "id=" + id +
                ", cuisineLabel='" + cuisineLabel + '\'' +
                '}';
    }
}
