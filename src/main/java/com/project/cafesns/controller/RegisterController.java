package com.project.cafesns.controller;


import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.register.RegisterResponseDto;
import com.project.cafesns.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    //승인목록조회
    @GetMapping("/api/registers/permission")
    public ResponseEntity<?> readok(Long httpRequest){
        Long userId = httpRequest;
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("승인 목록 조회에 성공헀습니다").data(registerService.readok()).build());
    }

    //거절목록조회
    @GetMapping("/api/registers/rejection")
    public ResponseEntity<?> readno(Long httpRequest){
        Long userId = httpRequest;
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("거절 목록 조회에 성공헀습니다").data(registerService.readno()).build());
    }
}

