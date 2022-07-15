package com.project.cafesns.controller;

import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.post.PostPatchDto;
import com.project.cafesns.model.dto.post.PostRequestDto;
import com.project.cafesns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final UserInfoInJwt userInfoInJwt;

    //게시글 작성
    @PostMapping("api/{cafeId}/posts")
    public ResponseEntity<?> addPost(@PathVariable Long cafeId,
                                     @RequestPart(value = "file") List<MultipartFile> fileList,
                                     @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     HttpServletRequest httpRequest) throws NoSuchAlgorithmException {
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        postService.addPost(cafeId, fileList, postRequestDto,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("게시글이 작성되었습니다.").build());
    }

    //게시글 수정
    @PatchMapping("api/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostPatchDto postPatchDto, HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        postService.updatePost(postId,postPatchDto,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("게시글이 수정에 성공했습니다.").build());
    }

    //게시글 삭제
    @DeleteMapping("api/posts/{postId}")
    public ResponseEntity<?>  deletePost(@PathVariable Long postId, HttpServletRequest httpRequest){
        userInfoInJwt.getUserInfo_InJwt(httpRequest.getHeader("Authorization"));
        Long userId = userInfoInJwt.getUserid();
        postService.deletePost(postId,userId);
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("게시글이 삭제되었습니다.").build());
    }
}
