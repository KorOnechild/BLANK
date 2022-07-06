package com.project.cafesns.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    EMAIL_DUBP_EXCEPTION(400),

    NICKNAME_DUBP_EXCEPTION(400),

    NOTMATCH_KEY_EXCEPTION(400),

    //수정 삭제시, 게시글, 코멘트 작성자가 아닐때
    NOTMATCH_USER_EXCEPTION(400);

    private final int status;
}
