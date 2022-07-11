package com.project.cafesns.controller;


import com.project.cafesns.model.dto.user.SigninReqeustDto;
import com.project.cafesns.model.dto.user.SignupRequestDto;
import com.project.cafesns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@RequestPart(value = "file") MultipartFile file,
                                    @RequestPart(value = "data") SignupRequestDto signupRequestDto) throws NoSuchAlgorithmException, RuntimeException {
        return userService.signup(file, signupRequestDto);
    }

    //로그인
    @PostMapping("/api/user/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqeustDto signinReqeustDto) throws NoSuchAlgorithmException {
        return userService.signin(signinReqeustDto);
    }
}

