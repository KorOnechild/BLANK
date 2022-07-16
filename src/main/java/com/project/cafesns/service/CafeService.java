package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.error.exceptions.user.NotExistUserException;
import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.*;
import com.project.cafesns.model.dto.search.SearchDto;
import com.project.cafesns.model.dto.search.SearchRequestDto;
import com.project.cafesns.model.entitiy.*;
import com.project.cafesns.repository.*;
import com.project.cafesns.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CafeService {
    //aws S3
    private final FileUploadService fileUploadService;
    private final UserInfoInJwt userInfoInJwt;
    private final PostRepository postRepository;
    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    // 카페 상세 페이지 배너조회
    public ResponseEntity<?> getBanner(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페 페이지가 존재하지 않습니다.")
        );

        List<Post> postList = postRepository.findAllByCafe(cafe);
        List<Image> imageList = new ArrayList<>();

        for(int i=0; i < postList.size(); i++){
            imageList.add(postList.get(i).getImageList().get(0));
            if(i == 2){break;}
        }

        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("배너 조회에 성공했습니다.")
                .data(CafeBannerDto.builder()
                        .imageList(imageList)
                        .logoimg(cafe.getUser() == null ? "" : cafe.getUser().getLogoimg())
                        .cafename(cafe.getCafename())
                        .avgstar(getAvgStar(postList))
                        .postCnt(postRepository.findAllByCafe(cafe).size())
                        .opentime(cafe.getOpentime())
                        .closetime(cafe.getClosetime())
                        .build())
                .build());
    }

    // 카페 상세 페이지 홈 조회
    public ResponseEntity<?> getHome(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페 페이지가 존재하지 않습니다.")
        );

        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("홈 조회에 성공했습니다.")
                .data(CafeHomeDto.builder()
                        .delivery(cafe.getDelivery())
                        .intro(cafe.getIntro())
                        .notice(cafe.getNotice())
                        .address(cafe.getAddress())
                        .addressdetail(cafe.getAddressdetail())
                        .zonenum(cafe.getZonenum())
                        .latitude(cafe.getLatitude())
                        .longitude(cafe.getLongitude())
                        .build())
                .build());
    }

    // 카페 상세 페이지 메뉴 조회
    public ResponseEntity<?> getMenus(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페 페이지가 존재하지 않습니다.")
        );
        List<Menu> menuList = menuRepository.findAllByCafe(cafe);
        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("메뉴 조회에 성공했습니다.")
                .data(CafeMenusDto.builder()
                        .menuList(getMenuListDto(menuList))
                        .build())
                .build());
    }

    // 사장님 카페 조회
    public ResponseEntity<?> getOwnerCafe(HttpServletRequest httpServletRequest) {

        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );

        Cafe cafe = cafeRepository.findByUser(user);

        if(user.getRole().equals("owner")){
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 홈 조회에 성공했습니다.")
                    .data(CafeDetailDto.builder()
                            .cafeid(cafe.getId())
                            .delivery(cafe.getDelivery())
                            .intro(cafe.getIntro())
                            .notice(cafe.getNotice())
                            .address(cafe.getAddress())
                            .addressdetail(cafe.getAddressdetail())
                            .zonenum(cafe.getZonenum())
                            .latitude(cafe.getLatitude())
                            .longitude(cafe.getLongitude())
                            .build())
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 조회
    public ResponseEntity<?> getOwnerCafeMenus(HttpServletRequest httpServletRequest) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);
        if(user.getId().equals(cafe.getUser().getId())){
            List<Menu> menuList = menuRepository.findAllByCafe(cafe);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴 조회에 성공했습니다.")
                    .data(CafeMenusDto.builder()
                            .menuList(getMenuListDto(menuList))
                            .build())
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    //menuListDto 생성 함수
    public List<MenuListDto> getMenuListDto(List<Menu> menuList){
        List<MenuListDto> menuListDtos = new ArrayList<>();

        for(Menu menu : menuList){
            menuListDtos.add(
                    MenuListDto.builder()
                            .menuid(menu.getId())
                            .category(menu.getCategory())
                            .menuname(menu.getMenuname())
                            .menuimg(menu.getMenuimg())
                            .menuprice(menu.getMenuprice())
                            .build());
        }
        return menuListDtos;
    }

    // 사장님 카페 홈 정보 수정
    //TODO : 변경 안된값은 그대로 보내줌
    @Transactional
    public ResponseEntity<?> modifyCafe(HttpServletRequest httpServletRequest,ModifyCafeRequestDto modifyCafeRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

        if(user.getId().equals(cafe.getUser().getId())){
            cafe.changeCafe(modifyCafeRequestDto);
            cafeRepository.save(cafe);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 홈 수정에 성공했습니다.")
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 등록
    public ResponseEntity<?> registMenu(HttpServletRequest httpServletRequest, MultipartFile file, RegistMenuRequestDto registMenuRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NotExistUserException(ErrorCode.NOTMATCH_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

        if(user.getId().equals(cafe.getUser().getId())){
            String munuImg = fileUploadService.uploadImage(file, "menu");
            menuRepository.save(Menu.builder().registMenuRequestDto(registMenuRequestDto).menuimg(munuImg).cafe(cafe).build());
            return ResponseEntity.ok().body(ResponseDto.builder().
                    result(true).
                    message("메뉴가 정상적으로 등록되었습니다.").
                    build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 수정
    @Transactional
    public ResponseEntity<?> modifyMenu(HttpServletRequest httpServletRequest,Long menuId, ModifyMenuDto modifyMenuDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NotExistUserException(ErrorCode.NOTMATCH_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 메뉴입니다.")
        );
        if(user.getId().equals(cafe.getUser().getId())){
            menu.changeMenu(modifyMenuDto);
            menuRepository.save(menu);
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴 수정에 성공했습니다")
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 삭제
    @Transactional
    public ResponseEntity<?> deleteMenu(HttpServletRequest httpServletRequest,Long menuId) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NotExistUserException(ErrorCode.NOTMATCH_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

        if(user.getId().equals(cafe.getUser().getId())){ // 본인 카페인지 확인하는 로직도 필요할 것 / 사장님인데 본인의 카페가 아닌경우
            Menu menu = menuRepository.findById(menuId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 메뉴입니다.")
            );
            menuRepository.delete(menu);
            int length = menu.getMenuimg().length();
            String filePath = menu.getMenuimg().substring(47,length); //
            fileUploadService.deleteFile(filePath);
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴가 정상적으로 삭제되었습니다.")
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }

    //카페 유무 조회 로직
    public ResponseEntity<?> getCafeExist(){
        List<Cafe> cafeList = cafeRepository.findAll();
        List<CafeDto> cafeDtos = new ArrayList<>();
        for(Cafe cafe : cafeList){
            cafeDtos.add(
                    CafeDto.builder()
                            .cafeid(cafe.getId())
                            .cafename(cafe.getCafename())
                            .address(cafe.getAddress())
                            .addressdetail(cafe.getAddressdetail())
                            .zonenum(cafe.getZonenum())
                            .build()
            );
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 조회에 성공했습니다.").data(cafeDtos).build());
    }

    //검색
    //TODO : 아직 Stream 사용법을 몰라서 키워드마다 Repository조회를 안해도 될거같고, 굉장히 비효율적인것 같다 Stream공부해서 전체 리스트 한번만 조회받고 거기서 찾아내는 방식을 시도해보자
    public ResponseEntity<?> search(String keyword){
        List<Cafe> cafeList = new ArrayList<>();
        //키워드가 카페 이름일 경우
        if(!cafeRepository.findAllByCafenameContains(keyword).isEmpty()){
            cafeList = cafeRepository.findAllByCafenameContains(keyword);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 이름을 통한 카페 검색").data(getSearchResult(cafeList)).build());
        }
        //키워드가 지역일 경우
        if(!cafeRepository.findAllByAddressContains(keyword).isEmpty()){
            cafeList = cafeRepository.findAllByAddressContains(keyword);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("지역을 통한 카페 검색").data(getSearchResult(cafeList)).build());
        } else if (!cafeRepository.findAllByAddressdetailContains(keyword).isEmpty()) {
            cafeList = cafeRepository.findAllByAddressdetailContains(keyword);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("상세정보를 통한 카페 검색").data(getSearchResult(cafeList)).build());
        }

        //키워드가 해시태그일 경우
        if(keyword.startsWith("#")){
            List<Hashtag> hashtagList = hashtagRepository.findAllByHashtagContains(keyword);
            List<Post> postList = new ArrayList<>();

            for(Hashtag hashtag : hashtagList){
                postList.add(hashtag.getPost());
            }
            for(Post post : postList){
                cafeList.add(post.getCafe());
            }
            LinkedHashSet<Cafe> linkedHashSet = new LinkedHashSet<>(cafeList);
            cafeList.clear();
            cafeList.addAll(linkedHashSet);
            return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("해시태그를 통한 카페 검색").data(getSearchResult(cafeList)).build());
        }
        return ResponseEntity.status(404).body(ResponseDto.builder().result(false).message("카페이름 또는 카페주소 또는 해시태그로 검색해주세요.").build());
    }

    //검색 결과 만드는 함수
    public List<SearchDto> getSearchResult(List<Cafe> cafeList){
        List<SearchDto> searchResult = new ArrayList<>();
        for(Cafe cafe : cafeList){
            searchResult.add(SearchDto.builder().cafeid(cafe.getId())
                    .cafeid(cafe.getId())
                    .cafename(cafe.getCafename())
                    .avgstar(getAvgStar(cafe.getPostList()))
                    .logoimg(cafe.getUser() == null ? "" : cafe.getUser().getLogoimg())
                    .address(cafe.getAddress())
                    .addressdetail(cafe.getAddressdetail())
                    .zonenum(cafe.getZonenum()).build());
        }
        return searchResult;
    }

    //카페 별점 평균 구하는 함수
    public float getAvgStar(List<Post> postList){
        float sumStar = 0F;
        for(Post post : postList){
            sumStar += post.getStar();
        }
        return postList.isEmpty() ? 0F : sumStar / postList.size();
    }

}




