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

    NOT_EXIST_USER_EXCEPTION(400),

    //신청
    ALREADY_EXIST_REGISTED_EXCEPTION(400),

    //카페
    ALREADY_EXIST_CAFE_EXCEPTION(400),

    //권한
    NOT_ALLOWED_EXCEPTION(400),

    //게시글
    POST_CREATE_EXCEPTION(400),

    //토큰
    NOTEXIST_TOKEN_EXCEPTION(400),

    REISSUE_TOKEN_EXCEPTION(401),

    BEARER_TOKEN_EXCEPTION(401),
    NULL_TOKEN_EXCEPTION(401);

    private final int status;
}
