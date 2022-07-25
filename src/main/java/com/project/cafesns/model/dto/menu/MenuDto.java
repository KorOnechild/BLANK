package com.project.cafesns.model.dto.menu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuDto {
    private Long menuid;
    private String menuname;
    private String menuimg;
    private int menuprice;
}
