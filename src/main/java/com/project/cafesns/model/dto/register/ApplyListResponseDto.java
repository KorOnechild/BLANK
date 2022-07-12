package com.project.cafesns.model.dto.register;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApplyListResponseDto {
    private List<RegistListDto> registerList;
}