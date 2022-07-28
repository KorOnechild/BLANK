package com.project.cafesns.model.dto.ouath;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OauthUserInfoDto {
    String email;
    String nickname;
    String profileimg;
}
