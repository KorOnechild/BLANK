package com.project.cafesns.validator;

import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.RefreshTokenRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public boolean checkDubpEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkDubpNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean checkexistRefreshToken(String refreshToken, User user){
        return refreshTokenRepository.existsRefreshTokenByRefreshtokenAndUser(refreshToken, user);
    }
}
