package br.com.trainingapi.workoutplanner.repository;

import br.com.trainingapi.workoutplanner.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long id);
}
