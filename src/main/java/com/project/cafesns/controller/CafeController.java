package com.project.cafesns.controller;


import com.project.cafesns.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    // 카페 상세 페이지 배너 조회
    @GetMapping("/api/cafes/{cafeId}")
    public ResponseEntity<?> getBanner (Long cafeId){
        return cafeService.getBanner(cafeId);
    }

    // 카페 상세 페이지 홈 조회
    @GetMapping("/api/cafes/{cafeId}/info")
    public ResponseEntity<?> getHome(Long cafeId){
        return cafeService.getHome(cafeId);
    }

    // 카페 상세 페이지 메뉴 조회
    @GetMapping("/api/cafes/{cafeId}/menus")
    public ResponseEntity<?> getMenus(Long cafeId){
        return cafeService.getMenus(cafeId);
    }
}

