package ru.recreation.recreationassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.recreation.recreationassistant.entity.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Dish findByDishLabel(String dishLabel);


}
