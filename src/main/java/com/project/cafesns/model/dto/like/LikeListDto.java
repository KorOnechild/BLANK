package com.project.cafesns.model.dto.like;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeListDto {
    private Long postid;
    private boolean like;
}
