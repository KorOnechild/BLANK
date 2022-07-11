package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Like;
import com.project.cafesns.model.entitiy.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Post> findAllByPost(Post post);
}
