package com.project.cafesns.model.dto.register;

import lombok.Getter;

@Getter
public class RegisterRequestDto {

    private String cafename;
    private String address;
    private String addressdetail;
    private String zonenum;
    private String latitude;
    private String longitude;

}