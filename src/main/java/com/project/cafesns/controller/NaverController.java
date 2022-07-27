package com.project.cafesns.controller;

import com.project.cafesns.model.dto.oauth.NaverOAuthDto;
import com.project.cafesns.service.oauth.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class NaverController {

    private final NaverService naverService;

    @GetMapping("/api/naver/auth")
    public String authNaver(@RequestParam ("code") String code, @RequestParam ("state") String state){
        String accessToken = naverService.requestAccessToken(naverService.generateAuthCodeRequest(code,state));
        String profile = naverService.generateProfileRequest(accessToken);
        return profile;
    }
}