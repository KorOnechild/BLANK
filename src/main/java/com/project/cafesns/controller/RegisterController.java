package com.project.cafesns.controller;


import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    //승인목록조회
    @GetMapping("/api/registers/permission")
    public ResponseEntity<?> readok(HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = UserInfoInJWT.getRole();
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("승인 목록 조회에 성공헀습니다").data(registerService.readok()).build());
    }

    //거절목록조회
    @GetMapping("/api/registers/rejection")
    public ResponseEntity<?> readno(HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = UserInfoInJWT.getRole();
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("거절 목록 조회에 성공헀습니다").data(registerService.readno()).build());
    }

    //미처리 내역 승인/거절
    @PatchMapping("/api/registers/{registerId}/{permit}")
    public ResponseEntity<?> permitset(@PathVariable Long registerId,
                                       @PathVariable  Boolean permit,HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = UserInfoInJWT.getRole();
        registerService.permitset(registerId,permit);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("내역변경에 성공헀습니다").build());
    }

    //관리자 카페생성 승인
    @PostMapping("/api/registers/{registerId}")
    public ResponseEntity<?> addcafe(@PathVariable Long registerId){
        registerService.addcafe(registerId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 생성에 성공헀습니다").build());
    }

    //관리자 승인카페삭제
    @DeleteMapping("/api/registers/{cafeId}")
    public ResponseEntity<?> deletecafe(@PathVariable Long cafeId){
        registerService.deletecafe(cafeId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 삭제에 성공헀습니다").build());
    }

    //등록된 모든 카페 조회
    @GetMapping("/api/registeredcafe")
    public ResponseEntity<?> showcafe(){
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("등록카페 조회에 성공헀습니다").data(registerService.showcafe()).build());
    }
}

