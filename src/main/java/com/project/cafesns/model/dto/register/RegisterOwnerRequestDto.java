package com.project.cafesns.model.dto.register;

import lombok.Getter;

@Getter
public class RegisterOwnerRequestDto {
    private String address;
    private String addressdetail;
    private String zonenum;
    private String latitude;
    private String longitude;
}