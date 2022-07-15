package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.model.dto.post.PostPatchDto;
import com.project.cafesns.model.dto.post.PostRequestDto;
import com.project.cafesns.model.entitiy.*;
import com.project.cafesns.repository.*;
import com.project.cafesns.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
    public void addPost(Long cafeId, MultipartFile [] fileList, PostRequestDto postRequestDto, Long userId) {
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

    @Transactional
    //게시글 수정
    public void updatePost(Long postId, PostPatchDto postPatchDto, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            post.changeContents(postPatchDto.getContent(),postPatchDto.getStar());
            postRepository.save(post);
        }
        else {
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    @Transactional
    //게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            deleteImage(post);
            postRepository.deleteById(post.getId());
        }
        else {
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    //연관 관계 맺어져 있는 데이터들 삭제하는 함수
    public void deleteImage(Post post){
        List<Image> imageList = imageRepository.findAllByPost(post);
        for(Image image : imageList){
            int length = image.getImg().length();
            String filePath = image.getImg().substring(47,length);
            fileUploadService.deleteFile(filePath);
        }
        System.out.printf("이미지 삭제가 완료되었습니다.");
    }
}