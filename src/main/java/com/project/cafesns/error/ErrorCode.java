package com.project.cafesns.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //사용자
    EMAIL_DUBP_EXCEPTION(400),
    NICKNAME_DUBP_EXCEPTION(400),
    NOTMATCH_USER_EXCEPTION(400),

    //토큰
    NOTEXIST_TOKEN_EXCEPTION(400),

    REISSUE_TOKEN_EXCEPTION(401),

    BEARER_TOKEN_EXCEPTION(401),
    NULL_TOKEN_EXCEPTION(401);

    private final int status;
}
