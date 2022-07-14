package com.project.cafesns.model.dto.user;

import lombok.Getter;

@Getter
public class ReissueTokenRequestDto {
    private String refreshToken;
    private String nickname;
}
