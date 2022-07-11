package com.project.cafesns.model.dto.register;

import com.project.cafesns.model.entitiy.Register;
import lombok.Builder;

import java.util.List;

@Builder
public class ApplyListResponseDto {
    private List<Register> registerList;
}