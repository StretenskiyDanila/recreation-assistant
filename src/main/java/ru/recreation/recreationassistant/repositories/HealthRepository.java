package ru.recreation.recreationassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.recreation.recreationassistant.entity.Health;

import java.util.Optional;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {

    Optional<Health> findByHealthLabel(String healthLabel);

}
