package com.project.cafesns.controller;


import com.project.cafesns.jwt.UserInfoInJwt;
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

    private final UserInfoInJwt userInfoInJwt;
    // 좋아요 여부 체크
    @GetMapping("/api/{postId}/like")
    public ResponseEntity<?> checkLike(HttpServletRequest httpServletRequest, @PathVariable Long postId ) throws NoSuchAlgorithmException {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        return likeService.checkLike(postId,userId);
    }

    // 좋아요 / 좋아요 취소
    @PostMapping("/api/{postId}/like")
    public ResponseEntity<?> upLike(HttpServletRequest httpServletRequest, @PathVariable Long postId) throws NoSuchAlgorithmException{
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        return likeService.upLike(postId,userId);
    }

    //카페 상세페이지 게시글 좋아요 여부 목록
    @GetMapping("/api/{cafeId}/like-list")
    public ResponseEntity<?> getCafeReviewsLikebyMe(HttpServletRequest httpServletRequest, @PathVariable Long cafeId){
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        return likeService.getCafeReviewsLikebyMe(cafeId, userId);
    }

    //마이페이지 게시글 좋아요 여부 목록
    @GetMapping("/api/user/like-list")
    public ResponseEntity<?> getMyReviewsLikebyMe(HttpServletRequest httpServletRequest){
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        return likeService.getMyReviewsLikebyMe(userId);
    }
}

