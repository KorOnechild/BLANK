package com.project.cafesns.model.entitiy;

import com.project.cafesns.model.dto.comment.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "postid")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    public Comment(CommentRequestDto commentRequestDto, User user, Post post){
        this.contents=commentRequestDto.getContents();
        this.user = user;
        this.post = post;
    }
    public void ChangeComment(String contents){
        this.contents=contents;
    }
}
