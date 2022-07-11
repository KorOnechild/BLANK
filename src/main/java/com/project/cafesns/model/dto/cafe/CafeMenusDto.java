package com.project.cafesns.model.dto.cafe;


import com.project.cafesns.model.entitiy.Menu;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeMenusDto {
    private List<Menu> menuList;
}