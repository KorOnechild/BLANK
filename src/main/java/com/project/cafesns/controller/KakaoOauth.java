package com.project.cafesns.controller;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class KakaoOauth {
    private final OauthService oauthService;

    @GetMapping("/api/oauth2/kakao")
    public ResponseEntity<?> kakaologin(@RequestParam ("code") String code) throws IOException {
        String token = oauthService.getAcToken(code);
        String redirect_uri="https://doridori.shop/?token=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirect_uri));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).body(ResponseDto.builder().result(true).message("게시글이 작성되었습니다.").data(token).build());
    }

    @GetMapping("/api/naver/auth")
    public void test(@RequestParam ("code") String code,@RequestParam ("state") String state){
        System.out.println(code);
        System.out.println(state);
    }
}
