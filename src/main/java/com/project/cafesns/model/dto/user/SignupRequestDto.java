package com.project.cafesns.model.dto.user;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String role;
    private String email;
    private String nickname;
    private String password;
    private String businessname;
    private String businessnum;
}
