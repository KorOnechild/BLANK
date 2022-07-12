package com.project.cafesns.controller;

import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final AdminService adminService;
    //JWT
    private final UserInfoInJwt userInfoInJwt;

    //승인목록조회
    @GetMapping("/api/registers/permission")
    public ResponseEntity<?> readok(HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = userInfoInJwt.getRole();
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("승인 목록 조회에 성공헀습니다").data(adminService.readok(userRole)).build());
    }

    //거절목록조회
    @GetMapping("/api/registers/rejection")
    public ResponseEntity<?> readno(HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = userInfoInJwt.getRole();
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("거절 목록 조회에 성공헀습니다").data(adminService.readno(userRole)).build());
    }

    //미처리 내역 승인/거절
    @PatchMapping("/api/registers/{registerId}/{permit}")
    public ResponseEntity<?> permitset(@PathVariable Long registerId,
                                       @PathVariable  Boolean permit,HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = userInfoInJwt.getRole();
        adminService.permitset(registerId,permit,userRole);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("내역변경에 성공헀습니다").build());
    }

    //관리자 카페생성 승인
    @PostMapping("/api/registers/{registerId}")
    public ResponseEntity<?> addcafe(@PathVariable Long registerId,HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = userInfoInJwt.getRole();
        adminService.addcafe(registerId,userRole);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 생성에 성공헀습니다").build());
    }

    //관리자 승인카페삭제
    @DeleteMapping("/api/registers/{cafeId}")
    public ResponseEntity<?> deletecafe(@PathVariable Long cafeId,HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = userInfoInJwt.getRole();
        adminService.deletecafe(cafeId,userRole);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 삭제에 성공헀습니다").build());
    }

    //등록된 모든 카페 조회
    @GetMapping("/api/registeredcafe")
    public ResponseEntity<?> showcafe(HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        String userRole = userInfoInJwt.getRole();
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("등록카페 조회에 성공헀습니다").data(adminService.showcafe(userRole)).build());
    }

    // 관리자 미처리 목록 조회
    @GetMapping("/api/registers")
    public ResponseEntity<?> getApplyList(HttpServletRequest  httpServletRequest){
        return adminService.getApplyList(httpServletRequest);
    }
}
