package ru.recreation.recreationassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.recreation.recreationassistant.entity.Cuisine;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

    Cuisine findByCuisineLabel(String cuisineLabel);


}
