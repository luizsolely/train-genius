package com.luizsolely.traingenius.repository;

import com.luizsolely.traingenius.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long id);
}
