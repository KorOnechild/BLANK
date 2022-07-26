package com.project.cafesns.model.dto.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
public class NaverOAuthDto {
    String access_token;
    String refresh_token;
    String token_type;
}
