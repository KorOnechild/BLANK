package com.project.cafesns.controller;


import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.comment.CommentRequestDto;
import com.project.cafesns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final UserInfoInJwt userInfoInJwt;
    private final CommentService commentService;

    //댓글 작성
    @PostMapping("api/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest){
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        String role = userInfoInJwt.getRole();
        commentService.addComment(postId,commentRequestDto,userId, role);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("댓글이 작성되었습니다!").build());
    }

    //댓글 수정
    @PatchMapping("api/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest){
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        String role = userInfoInJwt.getRole();
        commentService.updateComment(commentId,commentRequestDto,userId, role);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("댓글이 수정되었습니다!").build());
    }

    //댓글 삭제
    @DeleteMapping("api/comments/{commentId}")
    public ResponseEntity<?>  deleteComment(@PathVariable Long commentId,HttpServletRequest httpServletRequest){
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        String role = userInfoInJwt.getRole();
        commentService.deleteComment(commentId,userId, role);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("댓글이 삭제되었습니다!").build());
    }
}

