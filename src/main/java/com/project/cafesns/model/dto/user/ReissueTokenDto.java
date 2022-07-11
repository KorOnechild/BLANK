package com.project.cafesns.model.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueTokenDto {
    private String accessToken;
    private String refreshToken;
}
