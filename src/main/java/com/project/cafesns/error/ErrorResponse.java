package com.project.cafesns.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int status;

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
    }
}
