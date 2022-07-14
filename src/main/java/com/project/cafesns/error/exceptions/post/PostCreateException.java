package com.project.cafesns.error.exceptions.post;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class PostCreateException extends RuntimeException{
    private final ErrorCode errorCode;

    public PostCreateException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
