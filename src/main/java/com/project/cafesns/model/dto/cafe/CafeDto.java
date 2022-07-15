package com.project.cafesns.model.dto.cafe;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CafeDto {
    private Long cafeid;
    private String cafename;
    private String address;
    private String addressdetail;
    private String zonenum;
}
