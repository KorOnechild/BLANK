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
    private final ImageRepository imageRepository;
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

        if(postList.size() < 3){
            for(int i=0; i < postList.size(); i++){
                imageList.add(postList.get(i).getImageList().get(0));
            }
        }else{
            for(int i=0; i < 3; i++){
                imageList.add(postList.get(i).getImageList().get(0));
            }
        }
        String logoimg = cafe.getUser().getLogoimg();
        String cafename = cafe.getCafename();

        float allstar = 0;
        for (int i = 0; i < postList.size(); i++) {
            allstar += postList.get(i).getStar();
        }
        float avgstar = allstar / postList.size();
        int postCnt = postRepository.findAllByCafe(cafe).size();
        String opentime = cafe.getOpentime();
        String closetime = cafe.getClosetime();

        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("배너 조회에 성공했습니다.")
                .data(CafeBannerDto.builder()
                        .imageList(imageList)
                        .logoimg(logoimg)
                        .cafename(cafename)
                        .avgstar(avgstar)
                        .postCnt(postCnt)
                        .opentime(opentime)
                        .closetime(closetime)
                        .build())
                .build());
    }

    // 카페 상세 페이지 홈 조회
    public ResponseEntity<?> getHome(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페 페이지가 존재하지 않습니다.")
        );
        Boolean delivery = cafe.getDelivery();
        String intro = cafe.getIntro();
        String notice = cafe.getNotice();
        String address = cafe.getAddress();
        String addressdetail = cafe.getAddressdetail();
        String zonenum = cafe.getZonenum();
        String latitude = cafe.getLatitude();
        String longitude = cafe.getLongitude();

        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("홈 조회에 성공했습니다.")
                .data(CafeHomeDto.builder()
                        .delivery(delivery)
                        .intro(intro)
                        .notice(notice)
                        .address(address)
                        .addressdetail(addressdetail)
                        .zonenum(zonenum)
                        .latitude(latitude)
                        .longitude(longitude)
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
            Long cafeId = cafe.getId();
            Boolean delivery = cafe.getDelivery();
            String intro = cafe.getIntro();
            String notice = cafe.getNotice();
            String address = cafe.getAddress();
            String addressdetail = cafe.getAddressdetail();
            String zonenum = cafe.getZonenum();
            String latitude = cafe.getLatitude();
            String longitude = cafe.getLongitude();

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 홈 조회에 성공했습니다.")
                    .data(CafeDetailDto.builder()
                            .cafeId(cafeId)
                            .delivery(delivery)
                            .intro(intro)
                            .notice(notice)
                            .address(address)
                            .addressdetail(addressdetail)
                            .zonenum(zonenum)
                            .latitude(latitude)
                            .longitude(longitude)
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
}