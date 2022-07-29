package com.project.cafesns.controller;


import com.project.cafesns.model.dto.user.ReissueTokenRequestDto;
import com.project.cafesns.model.dto.user.SigninReqeustDto;
import com.project.cafesns.model.dto.user.SignupRequestDto;
import com.project.cafesns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입 (이미지 첨부시)
    @PostMapping("/api/user/signup-image")
    public ResponseEntity<?> signup(@RequestPart(value = "file", required = false) MultipartFile file,
                                    @RequestPart(value = "data") SignupRequestDto signupRequestDto) throws NoSuchAlgorithmException, RuntimeException {

        return userService.signup(file, signupRequestDto);
    }

    //회원가입(이미지 미첨부시)
    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) throws NoSuchAlgorithmException, RuntimeException {
            return userService.signup(null, signupRequestDto);
    }

    //로그인
    @PostMapping("/api/user/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqeustDto signinReqeustDto) throws NoSuchAlgorithmException {
        return userService.signin(signinReqeustDto);
    }

    //로그아웃
    @PostMapping("/api/user/signout")
    public ResponseEntity<?> signout(HttpServletRequest httpServletRequest){
        return userService.signout(httpServletRequest);
    }

    //액세스 토큰 재발급
    @PostMapping("/api/auth/refresh")
    public ResponseEntity<?> reissueAccessToken(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto) throws RuntimeException{
        return userService.reissueAccessToken(reissueTokenRequestDto);
    }

    @PatchMapping("/api/user/profile")
    public ResponseEntity<?> changeProfileimg(@RequestPart(value = "file") MultipartFile file, HttpServletRequest httpServletRequest){
        return userService.changeProfileimg(file, httpServletRequest);
    }
}

