package com.project.cafesns.service;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.like.LikeByMeDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Like;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.LikeRepository;
import com.project.cafesns.repository.PostRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CafeRepository cafeRepository;



    // 좋아요 체크 로직
    public ResponseEntity<?> checkLike(Long postId, Long userId) {//NosuchAlgorithmException 쓰는이유 : SHA256 함수 사용시 encrypt함수가있는데 이거 쓸때 예외처리 해주는 거에요
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 유저입니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글 입니다.")
        );
        if(likeRepository.existsByUserAndPost(user, post)){
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("좋아요한 게시물입니다.").build());
        }else{
            return ResponseEntity.ok().body(ResponseDto.builder().result(false).message("좋아요하지않은 게시물입니다.").build());
        }
    }

    // 게시글 좋아요
    public ResponseEntity<?> upLike(Long postId, Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 유저입니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글 입니다.")
        );

        if(!likeRepository.existsByUserAndPost(user, post)){
            Like like = new Like(user,post);
            likeRepository.save(like);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("좋아요 반영되었습니다.").build());
        }else{
            Like like = likeRepository.getLikeByUserAndPost(user,post);
            likeRepository.delete(like);
            return ResponseEntity.ok().body(ResponseDto.builder().result(false).message("좋아요가 취소되었습니다.").build());
        }
    }

    //카페 상세페이지 게시글 좋아요 여부 목록
    public ResponseEntity<?> getCafeReviewsLikebyMe(Long cafeId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NullPointerException("해당하는 유저가 없습니다.")
        );

        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("해당하는 카페가 없습니다.")
        );

        List<Post> postList = postRepository.findAllByCafeOrderByModifiedAtDesc(cafe);
        List<LikeByMeDto> likeByMeDtos = new ArrayList<>();
        for(Post post : postList){
             likeByMeDtos.add(LikeByMeDto.builder().postid(post.getId()).like(likeRepository.existsByUserAndPost(user, post)).build());
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 상세페이지 게시글 좋아요 여부 목록을 조회했습니다.").data(likeByMeDtos).build());
    }

    public ResponseEntity<?> getMyReviewsLikebyMe(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NullPointerException("해당하는 유저가 없습니다.")
        );
        List<Post> postList = postRepository.findAllByUserOrderByModifiedAtDesc(user);
        List<LikeByMeDto> likeByMeDtos = new ArrayList<>();
        for(Post post : postList){
            likeByMeDtos.add(LikeByMeDto.builder().postid(post.getId()).like(likeRepository.existsByUserAndPost(user, post)).build());
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("마이 페이지 게시글 좋아요 여부 목록을 조회했습니다.").data(likeByMeDtos).build());
    }
}
