package com.project.cafesns.service;

import com.project.cafesns.model.dto.ouath.KakaoAthenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthService {

    @Value("${client_id}")
    private String client_id;

    @Value("${redirect_uri}")
    private String redirect_uri;
    //액세스 토큰 요청
    public String getAcToken(String code) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code&client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&code=" + code);
        String body = sb.toString();
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<KakaoAthenResponseDto> responseEntity = rest.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, requestEntity, KakaoAthenResponseDto.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        KakaoAthenResponseDto response = responseEntity.getBody();
        System.out.println("Response status: " + status);
        System.out.println(response.getAccess_token());
        return maketoken(response.getAccess_token());
    }
    // 액세스 토큰을 통해 토큰을 만드는 로직
    public String maketoken(String accToken) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Bearer " + accToken);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<KakaoUserInfoResponseDto> responseEntity = rest.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, requestEntity, KakaoUserInfoResponseDto.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        KakaoUserInfoResponseDto response = responseEntity.getBody();
        String nickname = response.getProperties().getNickname();
        String profileimg = response.getProperties().getThumbnail_image();
        String email = response.getKakao_account().getEmail();
        System.out.println("Response status: " + status);
        System.out.println(nickname + profileimg + email);


        return accToken;

    }

    static class KakaoUserInfoResponseDto {

        private Properties properties;

        private Kakao_account kakao_account;

        public Properties getProperties() {
            return properties;
        }

        public Kakao_account getKakao_account() {
            return kakao_account;
        }

        static class Properties {
            private String nickname;
            private String thumbnail_image;

            public String getNickname() {
                return nickname;
            }

            public String getThumbnail_image() {
                return thumbnail_image;
            }
        }

        static class Kakao_account {
            private String email;

            public String getEmail() {
                return email;
            }
        }
    }

}
