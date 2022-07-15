package com.project.cafesns.model.dto.register;

import com.project.cafesns.model.entitiy.Register;
import lombok.Getter;

@Getter
public class RegisterResponseDto {
    Long registerid;
    String cafename;
    String address;
    String addressdetail;
    String zonenum;
    Boolean permit;
    Long cafeId;

    public RegisterResponseDto(Register register){
        this.registerid=  register.getId();
        this.cafename = register.getCafename();
        this.address = register.getAddress();
        this.addressdetail = register.getAddressdetail();
        this.zonenum = register.getZonenum();
        this.permit = register.getPermit();
    }
}
