package br.com.trainingapi.workoutplanner.repository;

import br.com.trainingapi.workoutplanner.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
