package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Image;
import com.project.cafesns.model.entitiy.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPost(Post post);
    void deleteAllByPost(Post post);
}
