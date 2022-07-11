package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);
}
