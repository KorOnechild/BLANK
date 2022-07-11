package com.project.cafesns.model.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninDto {
    private String nickname;
    private String role;
    private String accessToken;
    private String refreshToken;
}
