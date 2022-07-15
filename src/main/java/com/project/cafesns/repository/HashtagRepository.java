package com.project.cafesns.repository;

import com.project.cafesns.model.entitiy.Hashtag;
import com.project.cafesns.model.entitiy.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByHashtagContains(String keyword);

    List<Hashtag> findAllByPost(Post post);

}
