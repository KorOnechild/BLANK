package com.project.cafesns.model.dto.menu;


import lombok.Getter;

@Getter
public class RegistMenuRequestDto {
    private String category;
    private String menuname;
    private int menuprice;
}