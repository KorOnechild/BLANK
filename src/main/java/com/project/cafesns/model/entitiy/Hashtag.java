package com.project.cafesns.model.entitiy;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String hashtag;

    @ManyToOne
    @JoinColumn(name = "postid")
    @JsonBackReference(value = "hashtag-post-FK")
    private Post post;

    public Hashtag(String hashkey, Post post ){
        this.post = post;
        this.hashtag= hashkey;
    }
}
