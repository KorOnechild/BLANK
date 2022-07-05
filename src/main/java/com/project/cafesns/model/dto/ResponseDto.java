package com.project.cafesns.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseDto<T> {

    private String message;
    private boolean result;
    private T data;
}
