package com.project.cafesns.model.dto.postlist;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class MainPostListDto {
    private Long postid;
    private Long cafeid;
    private String img;
    private String cafename;
}
