package com.project.cafesns.model.dto.postlist;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostListDto {
    private Long postid;
    private String nickname;
    private List<ImageDto> image;
    private List<HashtagDto> hashtagList;
    private LocalDateTime modifiedAt;
    private int star;
    private int likecnt;
    private int commentCnt;
    private List<CommentDto> commentList;
}
