package br.com.trainingapi.workoutplanner.repository;

import br.com.trainingapi.workoutplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
