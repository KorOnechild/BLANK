package com.project.cafesns.result;

import com.project.cafesns.model.entitiy.Post;

import java.util.List;

public interface Calculator {
    //평균 별점 구하기
    float getAvgStar(List<Post> postList);


}
