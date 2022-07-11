package com.project.cafesns.jwt;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Component
public class UserInfoInJwt {

    private final JwtTokenProvider jwtTokenProvider;

    private String email;
    private Long userid;
    private String nickname;
    private String role;

//    public String getUserInfo_InJWT(String authorization) {
//        String accessToken = authorization.substring(7);
//        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
//        return (String) accessClaims.get("email");
//    }

    public void getUserInfo_InJwt(String authorization) {
        String accessToken = authorization.substring(7);
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        this.email = accessClaims.get("email", String.class);
        this.userid = accessClaims.get("userId", Long.class);
        this.nickname = accessClaims.get("nickname", String.class);
        this.role = accessClaims.get("role", String.class);
    }
}
