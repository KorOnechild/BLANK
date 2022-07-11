package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.user.NotmatchUserException;
import com.project.cafesns.model.dto.post.PostPatchDto;
import com.project.cafesns.model.dto.post.PostRequestDto;
import com.project.cafesns.model.entitiy.*;
import com.project.cafesns.repository.*;
import com.project.cafesns.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    //aws S3
    private final FileUploadService fileUploadService;

    //repository 의존성 추가
    private final PostRepository postRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final HashtagRepository hashtagRepository;

    //게시글 작성
    public void addPost(Long cafeId, List<MultipartFile> fileList, PostRequestDto postRequestDto, Long userId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow( () -> new NullPointerException("해당 카페가 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow( () -> new NullPointerException("해당 유저가 존재하지 않습니다."));
        List<String>hashkeys = postRequestDto.getHashtag();

        List<String> imageUrlList = fileUploadService.uploadImageList(fileList, "post");

        Post post = new Post(postRequestDto,user,cafe);
        postRepository.save(post);

        for(String imageUrl : imageUrlList){
            imageRepository.save(Image.builder().post(post).imageUrl(imageUrl).build());
        }

        for(String hashkey:hashkeys){
            Hashtag hashtag = new Hashtag(hashkey,post);
            hashtagRepository.save(hashtag);
        }
    }

    //게시글 수정
    public void updatePost(Long postId, PostPatchDto postPatchDto, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            post.setContents(postPatchDto.getContent());
            post.setStar(postPatchDto.getStar());
            postRepository.save(post);
        }
        else {
            throw new NotmatchUserException(ErrorCode.NOTMATCH_USER_EXCEPTION);
        }
    }

    //게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            postRepository.deleteById(postId);
        }
        else {
            throw new NotmatchUserException(ErrorCode.NOTMATCH_USER_EXCEPTION);
        }
    }
}