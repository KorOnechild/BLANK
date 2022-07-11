package com.project.cafesns.service;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.post.PostListDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.PostListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostListService {

    private final PostListRepository postListRepository;
    private final CafeRepository cafeRepository;
    public ResponseEntity<?> getPostListOrderByDesc(String region) {
        List<Cafe> cafeList = cafeRepository.findAllByAddressContaining(region);
        List<PostListDto> postListDtos = new ArrayList<>();


        for(Cafe cafe : cafeList){
            List<Post> postList = postListRepository.findAllByCafeOrderByModifiedAtDesc(cafe);
            for(Post post : postList){PostListDto postListDto = new PostListDto();
                postListDto.setCafeid(cafe.getId());
                postListDto.setCafename(cafe.getCafename());
                postListDto.setImg(post.getImgList().get(0));
                postListDto.setPostid(post.getId());
                postListDtos.add(postListDto);
            }
        }

        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("지역목록 조회에 성공했습니다."))
    }
}
