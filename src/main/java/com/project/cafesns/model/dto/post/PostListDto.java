package com.project.cafesns.model.dto.post;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class PostListDto {
    private Long postid;
    private Long cafeid;
    private String img;
    private String cafename;
}
