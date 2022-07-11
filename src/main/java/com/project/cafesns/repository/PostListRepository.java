package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostListRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCafeOrderByModifiedAtDesc(Cafe cafe);
    List<Post> findAllByUserOrderByModifiedAtDesc(User user);
}
