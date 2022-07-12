package com.project.cafesns.controller;


import com.project.cafesns.model.dto.cafe.ModifyCafeRequestDto;
import com.project.cafesns.model.dto.cafe.RegistMenuRequestDto;
import com.project.cafesns.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    // 사장님 카페조회
    @GetMapping("/api/owner/info")
    public ResponseEntity<?> getOwnerCafe(HttpServletRequest httpServletRequest){ // 카페아이디를 알아오고싶어요 userId가 필요할 것 같습니다
        return cafeService.getOwnerCafe(httpServletRequest);
    }

    // 사장님 카페 메뉴 조회
    @GetMapping("/api/owner/menus")
    public ResponseEntity<?> getOwnerCafeMenus(HttpServletRequest httpServletRequest){
        return cafeService.getOwnerCafeMenus(httpServletRequest);
    }

    // 카페 홈 정보 수정
    @PatchMapping("/api/owner/info")
    public ResponseEntity<?> modifyCafe(HttpServletRequest httpServletRequest, ModifyCafeRequestDto modifyCafeRequestDto){
        return cafeService.modifyCafe(httpServletRequest, modifyCafeRequestDto);
    }

    // 카페 메뉴 등록
    @PostMapping("/api/owner/menus")
    public ResponseEntity<?> registMenu(HttpServletRequest httpServletRequest, RegistMenuRequestDto registMenuRequestDto){
        return cafeService.registMenu(httpServletRequest,registMenuRequestDto);
    }

    // 카페 메뉴 수정
    @PatchMapping("/api/owner/menus/{menuId}")
    public ResponseEntity<?> modifyCafeMenu(HttpServletRequest httpServletRequest, Long menuId){
        return cafeService.modifyMenu(httpServletRequest, menuId);
    }

    // 카페 메뉴 삭제
    @DeleteMapping("/api/owner/menus/{menuId}")
    public ResponseEntity<?> deleteCafeMenu(HttpServletRequest httpServletRequest, Long menuId){
        return cafeService.deleteMenu(httpServletRequest, menuId);
    }

}

