package com.project.cafesns.controller;


import com.project.cafesns.model.dto.register.RegisterOwnerRequestDto;
import com.project.cafesns.model.dto.register.RegisterRequestDto;
import com.project.cafesns.repository.UserRepository;
import com.project.cafesns.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;


    // 카페 신청 (일반유저)
    @PostMapping("/api/user/regist-cafe")
    public ResponseEntity<?> applyCafe(HttpServletRequest httpServletRequest,@RequestBody RegisterRequestDto registerRequestDto) {
        return registerService.applyCafe(httpServletRequest,registerRequestDto);
    }

    // 카페 등록 (사장님)
    @PostMapping("/api/owner/regist-cafe")
    public ResponseEntity<?> registCafe(HttpServletRequest httpServletRequest,@RequestBody RegisterOwnerRequestDto registerOwnerRequestDto) {
        return registerService.registCafe(httpServletRequest,registerOwnerRequestDto);
    }
}
