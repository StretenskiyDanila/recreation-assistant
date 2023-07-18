package ru.recreation.recreationassistant.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "w_meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meal_label")
    private String mealLabel;

    public Meal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMealLabel() {
        return mealLabel;
    }

    public void setMealLabel(String mealLabel) {
        this.mealLabel = mealLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(id, meal.id) && Objects.equals(mealLabel, meal.mealLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mealLabel);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", mealLabel='" + mealLabel + '\'' +
                '}';
    }
}
