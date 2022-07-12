package com.project.cafesns.service;

import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.CafeDto;
import com.project.cafesns.model.dto.search.SearchRequestDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    //카페 유무 조회 로직
    public ResponseEntity<?> getCafeExist(String cafename){
        List<Cafe> cafeList = cafeRepository.findAllByCafename(cafename);
        List<CafeDto> cafeDtos = new ArrayList<>();
        for(Cafe cafe : cafeList){
            cafeDtos.add(
                    CafeDto.builder()
                            .cafename(cafe.getCafename())
                            .address(cafe.getAddress())
                            .addressdetail(cafe.getAddressdetail())
                            .zonenum(cafe.getZonenum())
                            .logoimg(cafe.getUser().getLogoimg())
                            .build()
            );
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 조회에 성공했습니다.").data(cafeDtos).build());
    }


    //검색
    public ResponseEntity<?> search(SearchRequestDto searchRequestDto){
        if(searchRequestDto.getKeyword().startsWith("#")){
            cafeRepository.fin
        }
    }
}
