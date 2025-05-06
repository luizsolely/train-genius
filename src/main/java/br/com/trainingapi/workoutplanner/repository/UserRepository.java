package br.com.trainingapi.workoutplanner.repository;

import br.com.trainingapi.workoutplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByAdminId(Long id);
}
