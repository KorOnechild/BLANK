package com.project.cafesns.model.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    int star;
    List<String> hashtag;
    String content;
}