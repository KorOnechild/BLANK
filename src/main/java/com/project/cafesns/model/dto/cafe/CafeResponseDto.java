package com.project.cafesns.model.dto.cafe;

import com.project.cafesns.model.entitiy.Cafe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CafeResponseDto {
    Long cafeId;
    String cafename;
    String address;
    String addressdetail;
    String zonenum;

    public CafeResponseDto(Cafe cafe){
        this.cafeId = cafe.getId();
        this.cafename = cafe.getAddress();
        this.address = cafe.getAddress();
        this.addressdetail = cafe.getAddressdetail();
        this.zonenum = cafe.getZonenum();
    }
}
