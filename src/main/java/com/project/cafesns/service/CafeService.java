package com.project.cafesns.service;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.CafeBannerDto;
import com.project.cafesns.model.entitiy.Image;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final CafeRepository cafeRepository;

    private final MenuRepository menuRepository;

    private final UserRepository userRepository;

    // 카페 상세 페이지 배너조회
    public ResponseEntity<?> getBanner(Long cafeId) {
        List<Post> postList = postRepository.findAllByCafeId(cafeId);
//       List<Image> imageList = postRepository.findByCafeId(cafeId).get();
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            imageList.add(imageRepository.findOneById(postList.get(i).getId()));
        }
        String logoimg = cafeRepository.findByCafeId(cafeId).getUser().getLogoimg();
        String cafename = cafeRepository.findByCafeId(cafeId).getCafename();
//       int avgstar = 0;
        int allstar = 0;
        for (int i = 0; i < postList.size(); i++) {
            allstar += postList.get(i).getStar();
        }
        int avgstar = allstar / postList.size();
        Long postCnt = Long.valueOf(postRepository.findAllByCafeId(cafeId).size());
        String opentime = cafeRepository.findByCafeId(cafeId).getOpentime();
        String closetime = cafeRepository.findByCafeId(cafeId).getClosetime();

        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("배너 조회에 성공했습니다.")
                .data(CafeBannerDto.builder()
                        .imageList(imageList)
                        .logoimg(logoimg)
                        .cafename(cafename)
                        .avgstar(avgstar)
                        .postCnt(postCnt)
                        .opentime(opentime)
                        .closetime(closetime)
                        .build())
                .build());

    }
}
