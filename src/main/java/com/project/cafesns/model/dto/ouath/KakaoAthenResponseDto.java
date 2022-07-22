package com.project.cafesns.model.dto.ouath;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAthenResponseDto {
    String token_type;
    String access_token;

}
