package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email, String encoded);

    boolean existsByNickname(String nickname);
    User findByEmail(String email);
    User findByNickname(String nickname);


}
