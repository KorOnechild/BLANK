package com.project.cafesns.error.exceptions.token;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class ReissueTokenException extends RuntimeException{
    private final ErrorCode errorCode;

    public ReissueTokenException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
