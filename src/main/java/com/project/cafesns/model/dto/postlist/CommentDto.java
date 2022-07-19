package com.project.cafesns.model.dto.postlist;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {
    private Long commentid;
    private String profileimg;
    private String nickname;
    private String contents;
}
