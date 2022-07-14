package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.error.exceptions.user.NotExistUserException;
import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.*;
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

    // 카페 상세 페이지 배너조회
    public ResponseEntity<?> getBanner(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페 페이지가 존재하지 않습니다.")
        );

        List<Post> postList = postRepository.findAllByCafe(cafe);
        List<Image> imageList = new ArrayList<>();
        float allstar = 0;

        for(int i=0; i < postList.size(); i++){
            imageList.add(postList.get(i).getImageList().get(0));
            if(i == 2){break;}
        }
        for (Post post : postList) {
            allstar += post.getStar();
        }
        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("배너 조회에 성공했습니다.")
                .data(CafeBannerDto.builder()
                        .imageList(imageList)
                        .logoimg(cafe.getUser() == null ? "" : cafe.getUser().getLogoimg())
                        .cafename(cafe.getCafename())
                        .avgstar(allstar / postList.size())
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
                            .cafeId(cafe.getId())
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
                            .menuId(menu.getId())
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
    public ResponseEntity<?> getCafeExist(String cafename){
        List<Cafe> cafeList = cafeRepository.findAllByCafenameContaining(cafename);
        List<CafeDto> cafeDtos = new ArrayList<>();
        String logoimg;
        for(Cafe cafe : cafeList){
            if(cafe.getUser() == null){
                logoimg = "";
            }else{
                logoimg = cafe.getUser().getLogoimg();
            }
            cafeDtos.add(
                    CafeDto.builder()
                            .cafename(cafe.getCafename())
                            .address(cafe.getAddress())
                            .addressdetail(cafe.getAddressdetail())
                            .zonenum(cafe.getZonenum())
                            .logoimg(logoimg)
                            .build()
            );
        }
        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 조회에 성공했습니다.").data(cafeDtos).build());
    }

    //    //검색
//    public ResponseEntity<?> search(SearchRequestDto searchRequestDto){
//        if(searchRequestDto.getKeyword().startsWith("#")){
//            cafeRepository.fin
//        }
//    }
}




