package com.project.cafesns.model.dto.postlist;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public class UserPostListDto {
    private Long postid;
    private String nickname;
    private List<ImageDto> image;
    private List<HashtagDto> hashtagList;
    private LocalDate modifiedAt;
    private int star;
    private int likecnt;
    private int commentCnt;
    private List<CommentDto> commentList;
}
