package ru.recreation.recreationassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.recreation.recreationassistant.entity.Meal;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Optional<Meal> findByMealLabel(String mealLabel);


}
