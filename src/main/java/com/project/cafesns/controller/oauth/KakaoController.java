package com.project.cafesns.controller.oauth;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.ouath.OauthLoginDto;
import com.project.cafesns.service.oauth.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService oauthService;

    @GetMapping("/api/oauth2/kakao")
    public ResponseEntity<?> kakaologin(@RequestParam ("code") String code) throws IOException {
        OauthLoginDto value = oauthService.getAcToken(code);
        String uri = UriComponentsBuilder.fromUriString("http://doridori.shop.s3-website.ap-northeast-2.amazonaws.com/Ouath")
                .encode()
                .queryParam("email",value.getEmail())
                .queryParam("nickname",value.getNickname())
                .queryParam("profileimg",value.getProfileimg())
                .queryParam("accessToken",value.getAccessToken())
                .queryParam("RefreshToken",value.getRefreshToken())
                .queryParam("role","user")
                .build().toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(uri));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).body(ResponseDto.builder()
                .result(true)
                .message("카카오 로그인이 완료되었습니다.")
                .build());
    }
}
