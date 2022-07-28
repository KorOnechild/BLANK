package com.project.cafesns.model.dto.ouath;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OauthLoginDto {
    String email;
    String nickname;
    String profileimg;
    String accessToken;
    String refreshToken;
}
