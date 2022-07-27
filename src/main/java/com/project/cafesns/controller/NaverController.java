package com.project.cafesns.controller;

import com.project.cafesns.model.dto.oauth.NaverOAuthDto;
import lombok.RequiredArgsConstructor;
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

//    @Value("${client-id}")
    private String clientid;

//    @Value("${client-secret}")
    private String clientsecret;

    @GetMapping("/api/naver/auth")
    public String authNaver(@RequestParam ("code") String code, @RequestParam ("state") String state){
        String accessToken = requestAccessToken(generateAuthCodeRequest(code,state));
        String profile = generateProfileRequest(accessToken);
        return profile;
    }

    // AccessToken 받기
    private HttpEntity<MultiValueMap<String,String>> generateAuthCodeRequest(String code, String state){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "qF6prEII7uI4LdzuKI5V");
        params.add("client_secret", "z2SQNRC_hm");
        params.add("code",code);
        params.add("state",state);
        return new HttpEntity<>(params, headers);
    }

    private String requestAccessToken(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverOAuthDto> responseEntity = restTemplate.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST, request, NaverOAuthDto.class);
        return responseEntity.getBody().getAccess_token();
    }

    private ResponseEntity<String> requestProfile(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, String.class);
    }

    private String generateProfileRequest(String accessToken){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        headers.add("Authorization","Bearer "+accessToken);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<NaverInfoOAuthDto> responseEntity = rest.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, requestEntity, NaverInfoOAuthDto.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();

        int status = httpStatus.value();

        NaverInfoOAuthDto response = responseEntity.getBody();

        String nickname = response.getResponse().getNickname();
        String profileImage = response.getResponse().getProfile_image();
        String email = response.getResponse().getEmail();

        System.out.println("Response status: "+status);
        System.out.println(nickname);
        System.out.println(profileImage);
        System.out.println(email);

        return nickname;
    }

    static class NaverInfoOAuthDto {
        public Response getResponse() {
            return response;
        }

        private Response response;

        static class Response{
            String nickname;

            String profile_image;

            String email;

            public String getNickname() {
                return nickname;
            }

            public String getProfile_image() {
                return profile_image;
            }

            public String getEmail() {
                return email;
            }
        }
    }

}