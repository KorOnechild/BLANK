package com.project.cafesns.result;


import com.project.cafesns.model.entitiy.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculatorImp implements Calculator {
    @Override

    //카페 별점 평균 구하는 함수
    public double getAvgStar(List<Post> postList){
        double sumStar = 0.0;
        for(Post post : postList){
            sumStar += post.getStar();
        }
        return postList.isEmpty() ? 0.0 : Math.floor(sumStar/postList.size() * 10.0)/10.0 ;
    }
}
