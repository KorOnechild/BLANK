package com.project.cafesns.model.dto.register;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class RegistListDto {
    private Long registerid;
    private String cafename;
    private String address;
    private String addressdetail;
    private String zonenum;
    private Boolean permit;
}
