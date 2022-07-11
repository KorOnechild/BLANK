package com.project.cafesns.model.dto.cafe;

import lombok.Builder;

@Builder
public class CafeDto {
    private String cafename;
    private String address;
    private String addressdetail;
    private String zonenum;
    private String logoimg;
}
