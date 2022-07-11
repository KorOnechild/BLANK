package com.project.cafesns.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //사용자
    EMAIL_DUBP_EXCEPTION(400),
    NICKNAME_DUBP_EXCEPTION(400),
    NOTMATCH_USER_EXCEPTION(400);

    private final int status;
}
