package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.NotMatchUserException;
import com.project.cafesns.model.dto.comment.CommentRequestDto;
import com.project.cafesns.model.entitiy.Comment;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.CommentRepository;
import com.project.cafesns.repository.PostRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

        public void addComment(Long postId, CommentRequestDto commentRequestDto, Long userId) {
            Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 카페가 존재하지 않습니다."));
            User user = userRepository.findById(userId).orElseThrow( () -> new NullPointerException("해당 유저가 존재하지 않습니다."));
            Comment comment = new Comment(commentRequestDto,user,post);
            commentRepository.save(comment);
        }


        public void updateComment(Long commentId, CommentRequestDto commentRequestDto, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));
        if(comment.getUser().getId().equals(userId)){
            comment.setContents(commentRequestDto.getContents());
            commentRepository.save(comment);
        }
        else {
            throw new NotMatchUserException(ErrorCode.NOTMATCH_USER_EXCEPTION);
        }
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));
        if(comment.getUser().getId().equals(userId)){
            commentRepository.deleteById(commentId);
        }
        else {
            throw new NotMatchUserException(ErrorCode.NOTMATCH_USER_EXCEPTION);
        }
    }
}

