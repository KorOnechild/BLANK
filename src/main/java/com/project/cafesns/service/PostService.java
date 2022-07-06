package com.project.cafesns.service;

import com.project.cafesns.model.dto.post.PostPatchDto;
import com.project.cafesns.model.dto.post.PostRequestDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Hashtag;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.HashtagRepository;
import com.project.cafesns.repository.PostRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    private final HashtagRepository hashtagRepository;

    //게시글 작성
    public Post addPost(Long cafeId, PostRequestDto postRequestDto, Long userId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow( () -> new NullPointerException("해당 카페가 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow( () -> new NullPointerException("해당 유저가 존재하지 않습니다."));
        List<String>hashkeys = postRequestDto.getHashtag();
        Post post = new Post(postRequestDto,user,cafe);
        for(String hashkey:hashkeys){
            Hashtag hashtag = new Hashtag(hashkey,post);
            hashtagRepository.save(hashtag);
        }
        postRepository.save(post);
        return post;
    }

    //게시글 수정
    public Post updatePost(Long postId, PostPatchDto postPatchDto, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            post.setContents(postPatchDto.getContent());
            post.setStar(postPatchDto.getStar());
            postRepository.save(post);
            return post;
        }
        else {
            throw new NullPointerException("다른사람의 게시글입니다");
        }
    }

    //게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            postRepository.deleteById(postId);
        }
        else {
            throw new NullPointerException("다른사람의 게시글입니다");
        }
    }
}
