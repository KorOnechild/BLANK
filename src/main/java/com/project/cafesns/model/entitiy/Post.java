package com.project.cafesns.model.entitiy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int star;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonBackReference(value = "user-post-FK")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cafeid")
    @JsonBackReference(value = "cafe-post-FK")
    private Cafe cafe;

    @OneToMany
    @JsonManagedReference(value = "hashtag-post-FK")
    private List<Hashtag> hashtagList;
}
