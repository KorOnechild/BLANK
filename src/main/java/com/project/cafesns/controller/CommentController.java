package com.project.cafesns.controller;


import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.comment.CommentRequestDto;
import com.project.cafesns.model.entitiy.Comment;
import com.project.cafesns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, Long httpRequest){
        Long userId = httpRequest;
        commentService.addComment(postId,commentRequestDto,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("댓글이 작성되었습니다!").build());
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, Long httpRequest){
        Long userId =httpRequest;
        commentService.updateComment(commentId,commentRequestDto,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("댓글이 수정되었습니다!").build());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?>  deleteComment(@PathVariable Long commentId,Long httpRequest){
        Long userId =httpRequest;
        commentService.deleteComment(commentId,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("댓글이 삭제되었습니다!").build());
    }

}

