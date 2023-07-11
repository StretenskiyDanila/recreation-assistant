package ru.recreation.recreationassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.recreation.recreationassistant.entity.Meal;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Meal findByMealLabel(String mealLabel);


}
