package com.project.cafesns.jwt;

import com.project.cafesns.model.entitiy.RefreshToken;
import com.project.cafesns.model.entitiy.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String SECRET_KEY = "sec";
    private final long ACCESS_TOKEN_VALID_TIME = 1 * 60 * 60 * 1000L;   // 1시간
    private final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L;   // 7일

    @PostConstruct
    protected void init() {
        System.out.println("SECRET_KEY: " + SECRET_KEY);
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims();//.setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("nickname", user.getNickname());
        claims.put("role", user.getRole());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 사용할 암호화 알고리즘과
                .compact();
    }


    public RefreshToken createRefreshToken(User user) {
        Date now = new Date();
        return RefreshToken.builder()
                .refreshtoken(UUID.randomUUID().toString())
                .expriyDate(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .user(user)
                .build();
    }

    //JWT 구문분석 함수
    public Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }
}
