package com.project.cafesns.controller;


import com.project.cafesns.model.dto.comment.CommentRequestDto;
import com.project.cafesns.model.entitiy.Comment;
import com.project.cafesns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/posts/{postId}/comments")
    public Comment addComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, Long httpRequest){
        Long userId = httpRequest;
        return commentService.addComment(postId,commentRequestDto,userId);
    }

    @PatchMapping("/comments/{commentId}")
    public Comment updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, Long httpRequest){
        Long userId =httpRequest;
        return commentService.updateComment(commentId,commentRequestDto,userId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void  deleteComment(@PathVariable Long commentId,Long httpRequest){
        Long userId =httpRequest;
        commentService.deleteComment(commentId,userId);
    }

}

