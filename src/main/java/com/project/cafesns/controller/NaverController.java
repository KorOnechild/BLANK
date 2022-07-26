package com.project.cafesns.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;

@RestController
@RequiredArgsConstructor
public class NaverController {

    @Value("${client-id}")
    private String clientid;

    @Value("${client-secret}")
    private String clientsecret;

    @GetMapping("/api/naver/auth")
    public String authNaver(@RequestParam ("code") String code, @RequestParam ("state") String state){
        String accessToken = requestAccessToken(generateAuthCodeRequest(code,state)).getBody();
        String profile = requestProfile(generateProfileRequest(accessToken)).getBody();
        System.out.println(profile);
        return profile;
//        return requestProfile(generateProfileRequest(accessToken)).getBody();
    }

    private HttpEntity<MultiValueMap<String,String>> generateAuthCodeRequest(String code, String state){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientid);
        params.add("client_secret", clientsecret);
        params.add("code",code);
        params.add("state",state);
        return new HttpEntity<>(params, headers);
    }

    private ResponseEntity<String> requestAccessToken(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    private ResponseEntity<String> requestProfile(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                request,
                String.class
        );
    }

    private HttpEntity<MultiValueMap<String,String>> generateProfileRequest(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }
}