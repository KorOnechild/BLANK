package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Comment;
import com.project.cafesns.model.entitiy.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostOrderByDesc(Post post);
}
