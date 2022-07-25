package com.project.cafesns.model.dto.menu;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MenuListDto {
    private List<MenuDto> drink;
    private List<MenuDto> dessert;
}
