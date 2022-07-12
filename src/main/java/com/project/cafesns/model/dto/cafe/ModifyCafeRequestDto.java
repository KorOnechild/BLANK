package com.project.cafesns.model.dto.cafe;

import lombok.Getter;

@Getter
public class ModifyCafeRequestDto {
    private String intro;
    private String notice;
    private String address;
    private String addressdetail;
    private String zonenum;
    private String latitude;
    private String longitude;
    private String opentime;
    private String closetime;
    private Boolean delivery;
}
