package com.luizsolely.traingenius.repository;

import com.luizsolely.traingenius.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByAdminId(Long id);
}
