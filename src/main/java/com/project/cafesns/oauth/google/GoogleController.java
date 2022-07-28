package com.project.cafesns.oauth.google;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.ouath.OauthLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/google")
public class GoogleController {

    private final ConfigUtils configUtils;
    private final GoogleService googleService;

    @GetMapping(value = "/login")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        String authUrl = configUtils.googleInitUrl();
        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/login/redirect")
    public ResponseEntity<?> redirectGoogleLogin(
            @RequestParam(value = "code") String code) {
        OauthLoginDto value = googleService.getAcToken(code);
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
                .message("구글 로그인이 완료되었습니다.")
                .build());
    }
}