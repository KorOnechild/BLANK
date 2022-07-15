package com.project.cafesns.model.dto.search;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchDto {
    private Long cafeid;
    private String cafename;
    private float avgstar;
    private String logoimg;
    private String address;
    private String addressdetail;
    private String zonenum;
}
