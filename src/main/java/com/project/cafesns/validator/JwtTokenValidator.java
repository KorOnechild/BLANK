package com.project.cafesns.validator;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.token.ReissueTokenException;
import com.project.cafesns.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    //AccessToken 유효성 검사
    public boolean isValidAccessToken(String token) {
        System.out.println("isValidToken is : " +token);
        try {
            Claims accessClaims = jwtTokenProvider.getClaimsFormToken(token);
            System.out.println("Access expireTime: " + accessClaims.getExpiration());
            System.out.println("Access email: " + accessClaims.get("email"));
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
            throw new ReissueTokenException(ErrorCode.REISSUE_TOKEN_EXCEPTION);
        } catch (JwtException exception) {
            System.out.println("Token Tampered");
            throw new ReissueTokenException(ErrorCode.REISSUE_TOKEN_EXCEPTION);
        }
    }
}
