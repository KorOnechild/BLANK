package com.project.cafesns.controller;

import com.project.cafesns.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoOauth {
    private final OauthService oauthService;

    @GetMapping("/api/oauth2/kakao")
    public void getAcToken(@RequestParam ("code") String code){
        oauthService.getAcToken(code);
    }
}
