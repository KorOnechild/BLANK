package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.entitiy.Like;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.LikeRepository;
import com.project.cafesns.repository.PostRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class LikeService {

    public UserRepository userRepository;

    public LikeRepository likeRepository;

    public PostRepository postRepository;

    // 좋아요 체크 로직
    public ResponseEntity<?> checkLike(Long postId, Long userId) throws NoSuchAlgorithmException {//NosuchAlgorithmException 쓰는이유
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new NoExistUserException(ErrorCode.NO_EXIST_USER_EXCEPTION)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NoExistPostException(ErrorCode.NO_EXIST_POST_EXCEPTION)
        );
        if(likeRepository.findByUserAndPost(user,post)==null){
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("좋아요한 게시물입니다.").build());
        }else{
            return ResponseEntity.ok().body(ResponseDto.builder().result(false).message("좋아요하지않은 게시물입니다.").build());
        }
    }

    // 게시글 좋아요
    public ResponseEntity<?> upLike(Long postId, Long userId) throws NoSuchAlgorithmException{
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new NoExistUserException(ErrorCode.NO_EXIST_USER_EXCEPTION)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NoExistPostException(ErrorCode.NO_EXIST_POST_EXCEPTION)
        );
        if(likeRepository.findByUserAndPost(user,post)==null){
            Like like = new Like(user,post);
            likeRepository.save(like);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("좋아요 반영되었습니다.").build());
        }else{
            Like like = likeRepository.getLikeByUserAndPost(user,post);
            likeRepository.delete(like);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("좋아요가 취소되었습니다.").build());
        }
    }

}
