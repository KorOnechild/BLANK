package com.project.cafesns.model.dto.cafe;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeMenusDto {
    private List<MenuListDto> menuList;
}