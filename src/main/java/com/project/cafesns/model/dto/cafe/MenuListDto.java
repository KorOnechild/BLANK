package com.project.cafesns.model.dto.cafe;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuListDto {
    private Long menuId;
    private String category;
    private String menuname;
    private String menuimg;
    private int menuprice;
}
