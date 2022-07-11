package com.project.cafesns.controller;

import com.project.cafesns.service.PostListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostListController {

    private PostListService postListService;

    @GetMapping("/api/posts/{region}")
    public ResponseEntity<?> getPostListOrderByDesc(@PathVariable String region){
        return postListService.getPostListOrderByDesc(region);
    }

    @GetMapping("/api/user/posts")
    public ResponseEntity<?> getUserPostList(HttpServletRequest request){

        return postListService.getUserPostList(request);
    }


}
