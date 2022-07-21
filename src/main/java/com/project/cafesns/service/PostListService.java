package com.project.cafesns.service;

import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.postlist.*;
import com.project.cafesns.model.entitiy.*;
import com.project.cafesns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostListService {

    private final UserInfoInJwt userInfoInJwt;

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postListRepository;
    private final CafeRepository cafeRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;


    //메인페이지 게시글 목록 최신순 조회
    public ResponseEntity<?> getPostListOrderByDesc(String region) {
        List<Cafe> cafeList = cafeRepository.findAllByAddressContains(region);
        List<Post> posts = new ArrayList<>();

        for(Cafe cafe : cafeList){
            List<Post> postList = postListRepository.findAllByCafeOrderByModifiedAtDesc(cafe);
            posts.addAll(postList);

        }

        List<Post> sortedList = posts.stream()
                .sorted(Comparator.comparing(Post :: getLocalDateTime).reversed())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("지역목록 조회에 성공했습니다.").data(getMainPostListDtos(sortedList)).build());
    }


    //마이페이지 게시글 목록 조회
    public ResponseEntity<?> getUserPostList(HttpServletRequest httpServletRequest) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NullPointerException("사용자 정보가 없습니다.")
        );

        List<Post> postList = postListRepository.findAllByUserOrderByModifiedAtDesc(user);
        List<PostListDto> PostListDtos = new ArrayList<>();

        for(Post post : postList){
            String msg = timegap(post);
            PostListDtos.add(
                    PostListDto.builder().postid(post.getId())
                            .nickname(post.getUser().getNickname())
                            .image(getImageDtoList(post))
                            .hashtagList(getHashtagDtoList(post))
                            .modifiedAt(msg)
                            .star(post.getStar())
                            .contents(post.getContents())
                            .likecnt(getLikeCnt(post))
                            .commentCnt(getCommentCnt(post))
                            .commentList(getCommentDtoList(post))
                            .build());
        }
        if (postList.isEmpty()) {
            return ResponseEntity.ok().body(ResponseDto.builder().result(false).message("작성한 리뷰가 없습니다.").build());
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("내가 쓴 리뷰 목록을 조회했습니다.").data(PostListDtos).build());
    }

    //카페 상세페이지 리뷰 목록 조회
    public ResponseEntity<?> getPostListInCafePage(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페가 존재하지 않습니다.")
        );

        List<Post> postList = postListRepository.findAllByCafeOrderByModifiedAtDesc(cafe);
        List<PostListDto> postListDtos = new ArrayList<>();

        for(Post post : postList){
            String msg=timegap(post);
            postListDtos.add(
                    PostListDto.builder()
                            .postid(post.getId())
                            .nickname(post.getUser().getNickname())
                            .image(getImageDtoList(post))
                            .hashtagList(getHashtagDtoList(post))
                            .modifiedAt(msg)
                            .contents(post.getContents())
                            .star(post.getStar())
                            .likecnt(getLikeCnt(post))
                            .commentCnt(getCommentCnt(post))
                            .commentList(getCommentDtoList(post))
                            .build());
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 상세페이지 리뷰 목록을 조회했습니다.").data(postListDtos).build());
    }

    /*---------------------------<비즈니스 로직에 필요한 함수들>--------------------------------*/

    //게시글 좋아요 개수 얻기 로직
    public int getLikeCnt(Post post){
        return likeRepository.findAllByPost(post).size();
    }

    //댓글 개수 얻기 로직
    public int getCommentCnt(Post post){
        return commentRepository.findAllByPost(post).size();
    }

    //imageDto 리스트 만들기 로직
    public List<ImageDto> getImageDtoList(Post post){
        List<Image> imageList = imageRepository.findAllByPost(post);
        List<ImageDto> imageDtos = new ArrayList<>();
        for(Image image : imageList){
            imageDtos.add(ImageDto.builder().img(image.getImg()).build());
        }
        return imageDtos;
    }


    //hashtagDto 리스트 만들기 로직
    public List<HashtagDto> getHashtagDtoList(Post post){
        List<Hashtag> hashtagList = hashtagRepository.findAllByPost(post);
        List<HashtagDto> hashtagDtos = new ArrayList<>();

        for(Hashtag hashtag : hashtagList){
            hashtagDtos.add(HashtagDto.builder().hashtag(hashtag.getHashtag()).build());
        }
        return hashtagDtos;
    }


    //commentDto 리스트 만들기 로직
    public List<CommentDto> getCommentDtoList(Post post){
        List<Comment> commentList = commentRepository.findAllByPostOrderByModifiedAtDesc(post);
        List<CommentDto> commentDtos = new ArrayList<>();
        for(Comment comment : commentList){
            commentDtos.add(
                    CommentDto.builder()
                    .commentid(comment.getId())
                    .profileimg(comment.getUser().getProfileimg())
                    .nickname(comment.getUser().getNickname())
                    .contents(comment.getContents())
                    .build());
        }
        return commentDtos;
    }

    //메인페이지 게시글 목록 형태 생성 함수
    public List<MainPostListDto> getMainPostListDtos(List<Post> postlist){
        List<MainPostListDto> mainPostListDtos = new ArrayList<>();
        for(Post post : postlist){
            mainPostListDtos.add(
                    MainPostListDto.builder()
                            .cafename(post.getCafe().getCafename())
                            .postid(post.getId())
                            .cafeid(post.getCafe().getId())
                            .img(post.getImageList().get(0).getImg())
                            .build()
            );
        }
        return mainPostListDtos;
    }

    public String timegap(Post post){
        LocalDateTime date =post.getModifiedAt();
        LocalDateTime localDateTime = LocalDateTime.now();
        Duration duration = Duration.between(date,localDateTime);
        Long testdate= duration.getSeconds();
        String msg =null;
        if(testdate<60){
            msg=testdate+"초전";
        } else if ((testdate/60)<60) {
            msg=(testdate/60)+"분전";
        } else if ((testdate/(60*60))<24) {
            msg=(testdate/(60*60))+"시간전";
        }else if ((testdate/(24*60*60))<30) {
            msg=(testdate/(24*60*60))+"일전";
        } else if ((testdate/(30*24*60*60))<365) {
            msg=(testdate/(30*24*60*60))+"달전";
        }else {
            msg=(testdate/(30*24*60*60*365))+"년전";
        }
        return msg;
    }
}

