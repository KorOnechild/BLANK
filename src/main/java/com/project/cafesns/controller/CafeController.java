package com.project.cafesns.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {


    @GetMapping("/api/cafe/{cafename}")
    public ResponseEntity<?> getCafeExist(@PathVariable String cafename){
        return cafeService.getCafeExist(cafename);
    }

}

