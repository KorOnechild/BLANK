package com.project.cafesns.controller;


import com.project.cafesns.model.dto.search.SearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {


    @GetMapping("/api/cafe/{cafename}")
    public ResponseEntity<?> getCafeExist(@PathVariable String cafename){
        return cafeService.getCafeExist(cafename);
    }

    @GetMapping("/api/search")
    public ResponseEntity<?> search(@RequestBody SearchRequestDto searchRequestDto){
        return cafeService.search(searchRequestDto);
    }

}

