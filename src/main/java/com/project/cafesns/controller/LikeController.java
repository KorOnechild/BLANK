package com.project.cafesns.controller;


import com.project.cafesns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 여부 체크
    @GetMapping("/api/{postId}/like")
    public ResponseEntity<?> checkLike(HttpServletRequest httpServletRequest, @PathVariable Long postId ) throws NoSuchAlgorithmException {
        Long userId = userInfoInJwt.getUserId_InJWT(httpServletRequest.getHeader("Authorization"));
        return likeService.checkLike(postId,userId);
    }

    // 좋아요 / 좋아요 취소
    @PostMapping("/api/{postId}/like")
    public ResponseEntity<?> upLike(HttpServletRequest httpServletRequest, @PathVariable Long postId) throws NoSuchAlgorithmException{
        Long userId = userInfoInJwt.getUserId_InJWT(httpServletRequest.getHeader("Authorization"));
        return likeService.upLike(postId,userId);
    }

}

