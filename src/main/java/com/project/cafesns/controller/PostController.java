package com.project.cafesns.controller;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.post.PostPatchDto;
import com.project.cafesns.model.dto.post.PostRequestDto;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/{cafeId}/posts")
    public ResponseEntity<?> addPost(@PathVariable Long cafeId,
                        @RequestBody PostRequestDto postRequestDto,
                        Long httpRequest) throws NoSuchAlgorithmException {
        Long userId =httpRequest;
        postService.addPost(cafeId,postRequestDto,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("게시글이 작성되었습니다.").build());
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostPatchDto postPatchDto, Long httpRequest){
        Long userId =httpRequest;
        postService.updatePost(postId,postPatchDto,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("게시글이 작성되었습니다.").build());
    }

    @DeleteMapping("/posts/{postId}")
    public void  deletePost(@PathVariable Long postId,Long httpRequest){
        Long userId =httpRequest;
        postService.deletePost(postId,userId);
    }
}
