package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Like;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Boolean existsByUserAndPost(User user, Post post);
    Like getLikeByUserAndPost(User user, Post post);
}
