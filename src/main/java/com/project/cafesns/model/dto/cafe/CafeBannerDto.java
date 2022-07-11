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
    private int avgstar;
    private Long postCnt;
    private String opentime;
    private String closetime;

    public CafeBannerDto(List<Image> imageList, String logoimg, String cafename, int avgstar, long postCnt, String opentime, String closetime) {
        this.imageList = imageList;
        this.logoimg = logoimg;
        this.cafename = cafename;
        this.avgstar = avgstar;
        this.postCnt = postCnt;
        this.opentime = opentime;
        this.closetime = closetime;
    }
}
