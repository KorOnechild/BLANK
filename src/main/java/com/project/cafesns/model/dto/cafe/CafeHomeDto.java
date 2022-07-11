package com.project.cafesns.model.dto.cafe;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CafeHomeDto {
    private Boolean delivery;
    private String intro;
    private String notice;
    private String address;
    private String addressdetail;
    private String zonenum;
    private String latitude;
    private String longitude;
}