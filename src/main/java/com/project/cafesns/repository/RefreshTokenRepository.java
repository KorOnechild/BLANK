package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.RefreshToken;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsRefreshTokenByRefreshtokenAndUser(String refreshToken, User user);

    RefreshToken findByUser(User user);
}
