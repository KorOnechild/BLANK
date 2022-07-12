package com.project.cafesns.model.dto.postlist;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public class MainPostListDto {
    private Long postid;
    private Long cafeid;
    private String img;
    private String cafename;
}
