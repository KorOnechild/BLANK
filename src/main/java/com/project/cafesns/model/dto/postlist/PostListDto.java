package com.project.cafesns.model.dto.postlist;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostListDto {
    private Long postid;
    private String profileimg;
    private String nickname;
    private List<ImageDto> image;
    private List<HashtagDto> hashtagList;
    private String modifiedAt;
    private int star;
    private int likecnt;
    private int commentCnt;
    private List<CommentDto> commentList;
    private String contents;
}
