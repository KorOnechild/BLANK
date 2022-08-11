package com.project.cafesns.model.entitiy;

import com.project.cafesns.model.Timestamped;
import com.project.cafesns.model.dto.register.RegisterRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Register extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cafename;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressdetail;

    @Column(nullable = false)
    private String zonenum;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column
    private Boolean permit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;

    @Builder
    public Register(RegisterRequestDto registerRequestDto, User user) {
        this.cafename = registerRequestDto.getCafename();
        this.address = registerRequestDto.getAddress();
        this.addressdetail = registerRequestDto.getAddressdetail();
        this.zonenum = registerRequestDto.getZonenum();
        this.latitude = registerRequestDto.getLatitude();
        this.longitude = registerRequestDto.getLongitude();
        this.permit = null;
        this.user = user;
    }

    public void changePermit(Boolean permit){
        this.permit = permit;
    }
}
