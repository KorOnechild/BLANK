package com.project.cafesns.model.dto.cafe;


import com.project.cafesns.model.entitiy.Cafe;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistMenuRequestDto {
    private String category;
    private String menuimg;
    private String menuname;
    private int menuprice;
}