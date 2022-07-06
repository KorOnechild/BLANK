package com.project.cafesns.model.entitiy;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Register {
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
    private Boolean permit;

    @Column
    private Long cafeid;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;
}
