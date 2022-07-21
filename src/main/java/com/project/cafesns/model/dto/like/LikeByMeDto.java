package com.project.cafesns.model.dto.like;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeByMeDto {
    private Long postid;
    private boolean like;
}
