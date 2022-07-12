package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Like;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByPost(Post post);

    Boolean existsByUserAndPost(User user, Post post);
    Like getLikeByUserAndPost(User user, Post post);
}
