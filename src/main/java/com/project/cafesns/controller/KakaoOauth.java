package com.project.cafesns.controller;

import com.project.cafesns.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoOauth {
    private final OauthService oauthService;

    @GetMapping("/api/oauth2/kakao")
    public void getAcToken(@RequestParam ("code") String code, HttpServletResponse response) throws IOException {
        String token = oauthService.getAcToken(code);
        String redirect_uri="https://doridori.shop/?token=" + token;
        response.sendRedirect(redirect_uri);
    }

    @GetMapping("/api/naver/auth")
    public void test(@RequestParam ("code") String code,@RequestParam ("state") String state){
        System.out.println(code);
        System.out.println(state);
    }
}
