package com.project.cafesns.service;

import com.project.cafesns.model.dto.comment.CommentRequestDto;
import com.project.cafesns.model.entitiy.Comment;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.CommentRepository;
import com.project.cafesns.repository.PostRepository;
import com.project.cafesns.repository.UserRepository;
import com.project.cafesns.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final UserValidator userValidator;

    public Long addComment(Long postId, CommentRequestDto commentRequestDto, Long userId, String role) {
        userValidator.userCheck(role);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 카페가 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 유저가 존재하지 않습니다."));
        Comment comment = new Comment(commentRequestDto, user, post);
        return commentRepository.save(comment).getId();
    }

    //댓글 수정 로직
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto commentRequestDto, Long userId, String role) {
        userValidator.userCheck(role);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));
         userValidator.authorCheck(comment.getId(),userId);
            comment.ChangeComment(commentRequestDto.getContents());
            commentRepository.save(comment);

    }
    //댓글 삭제 로직
    @Transactional
    public void deleteComment(Long commentId, Long userId, String role) {
        userValidator.userCheck(role);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));
        userValidator.authorCheck(comment.getId(),userId);
            commentRepository.deleteById(commentId);
    }
}

