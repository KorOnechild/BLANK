package com.project.cafesns.model.dto.cafe;

import com.project.cafesns.model.entitiy.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeBannerDto {
    private List<Image> imageList;
    private String logoimg;
    private String cafename;
    private double avgstar;
    private int postCnt;
    private String opentime;
    private String closetime;
}
