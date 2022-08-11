package com.project.cafesns.model.entitiy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    @JsonBackReference(value = "image-post-FK")
    private Post post;

    @Column
    private String img;
    @Builder
    public Image(Post post, String imageUrl){
        this.post = post;
        this.img = imageUrl;
    }
}
