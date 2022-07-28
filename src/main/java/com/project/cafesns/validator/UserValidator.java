package com.project.cafesns.validator;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.error.exceptions.token.NotExistTokenException;
import com.project.cafesns.error.exceptions.user.EmailDupblicateException;
import com.project.cafesns.error.exceptions.user.NicknameDubplicateException;
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

    //이메일 중복 체크
    public void checkDubpEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new EmailDupblicateException(ErrorCode.EMAIL_DUBP_EXCEPTION);
        }
    }

    //닉네임 중복 체크
    public void checkDubpNickname(String nickname) {
        if(userRepository.existsByNickname(nickname)){
            throw new NicknameDubplicateException(ErrorCode.NICKNAME_DUBP_EXCEPTION);
        }
    }

    //존재하지 않는 토큰이라는 예외처리
    public void checkexistRefreshToken(String refreshToken, User user){
        if(!refreshTokenRepository.existsRefreshTokenByRefreshtokenAndUser(refreshToken, user)){
            throw new NotExistTokenException(ErrorCode.NOTEXIST_TOKEN_EXCEPTION);
        }
    }

    //유저 체크 로직
    public void userCheck(String role) {
        if (!role.equals("user")) {
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    //작성자 체크 로직
    public void authorCheck(Long authorid,Long userid){
        if(!authorid.equals(userid)){
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }
}
