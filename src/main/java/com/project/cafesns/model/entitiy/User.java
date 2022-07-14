package com.project.cafesns.model.entitiy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.cafesns.model.dto.user.SignupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private String businessname;

    @Column
    private String businessnum;

    @Column
    private String profileimg;

    @Column
    private String logoimg;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-cafe-FK")
    private List<Cafe> cafeList;



    @Builder
    public User(SignupRequestDto signupRequestDto, String encodedPw, String profileimg, String logoimg){
        this.email = signupRequestDto.getEmail();
        this.role = signupRequestDto.getRole();
        this.nickname = signupRequestDto.getNickname();
        this.password = encodedPw;
        this.businessname = signupRequestDto.getBusinessname();
        this.businessnum = signupRequestDto.getBusinessnum();
        this.profileimg = profileimg;
        this.logoimg = logoimg;
    }
}