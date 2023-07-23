package ru.recreation.recreationassistant.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "w_dish")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_label")
    private String dishLabel;

    public Dish() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishLabel() {
        return dishLabel;
    }

    public void setDishLabel(String dishLabel) {
        this.dishLabel = dishLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(dishLabel, dish.dishLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dishLabel);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", dishLabel='" + dishLabel + '\'' +
                '}';
    }
}
