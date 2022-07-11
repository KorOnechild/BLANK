package com.project.cafesns.model.dto.postlist;

import lombok.Builder;

@Builder
public class CommentDto {
    private String profileimg;
    private String nickname;
    private String contents;
}
