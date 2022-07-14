package com.project.cafesns.model.entitiy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.cafesns.model.Timestamped;
import com.project.cafesns.model.dto.post.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
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


    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference(value = "image-post-FK")
    private List<Image> imageList;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference(value = "hashtag-post-FK")
    private List<Hashtag> hashtagList;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference(value = "comment-post-FK")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference(value = "like-post-FK")
    private List<Like> likeList;

    public Post(PostRequestDto postRequestDto, User user, Cafe cafe){
        this.contents = postRequestDto.getContent();
        this.star = postRequestDto.getStar();
        this.user = user;
        this.cafe = cafe;
    }

    public void changeContents(String contents, int star){
        this.contents = contents;
        this.star  = star;
    }


    public static LocalDateTime getLocalDateTime(Post o) {
        return o.getModifiedAt();
    }
}
