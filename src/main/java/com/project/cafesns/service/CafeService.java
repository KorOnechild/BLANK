package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.*;
import com.project.cafesns.model.entitiy.*;
import com.project.cafesns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 카페 상세 페이지 배너조회
    public ResponseEntity<?> getBanner(Long cafeId) {
        List<Post> postList = postRepository.findAllByCafeId(cafeId);
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            imageList.add(imageRepository.findOneById(postList.get(i).getId()));
        }
        String logoimg = cafeRepository.findByCafeId(cafeId).getUser().getLogoimg();
        String cafename = cafeRepository.findByCafeId(cafeId).getCafename();
//       int avgstar = 0;
        int allstar = 0;
        for (int i = 0; i < postList.size(); i++) {
            allstar += postList.get(i).getStar();
        }
        int avgstar = allstar / postList.size();
        Long postCnt = Long.valueOf(postRepository.findAllByCafeId(cafeId).size());
        String opentime = cafeRepository.findByCafeId(cafeId).getOpentime();
        String closetime = cafeRepository.findByCafeId(cafeId).getClosetime();

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
        Boolean delivery = cafeRepository.findByCafeId(cafeId).getDelivery();
        String intro = cafeRepository.findByCafeId(cafeId).getIntro();
        String notice = cafeRepository.findByCafeId(cafeId).getNotice();
        String address = cafeRepository.findByCafeId(cafeId).getAddress();
        String addressdetail = cafeRepository.findByCafeId(cafeId).getAddressdetail();
        String zonenum = cafeRepository.findByCafeId(cafeId).getZonenum();
        String latitude = cafeRepository.findByCafeId(cafeId).getLatitude();
        String longitude = cafeRepository.findByCafeId(cafeId).getLongitude();

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
        List<Menu> menuList = cafeRepository.findAllByCafeId(cafeId);
        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("메뉴 조회에 성공했습니다.")
                .data(CafeMenusDto.builder()
                        .menuList(menuList)
                        .build())
                .build());

    }

    // 사장님 카페조회
    public ResponseEntity<?> getOwnerCafe(HttpServletRequest httpServletRequest) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );

        if(user.getRole().equals("owner")){
            Long cafeId = cafeRepository.findByUser(user).getId();
            Boolean delivery = cafeRepository.findByCafeId(cafeId).getDelivery();
            String intro = cafeRepository.findByCafeId(cafeId).getIntro();
            String notice = cafeRepository.findByCafeId(cafeId).getNotice();
            String address = cafeRepository.findByCafeId(cafeId).getAddress();
            String addressdetail = cafeRepository.findByCafeId(cafeId).getAddressdetail();
            String zonenum = cafeRepository.findByCafeId(cafeId).getZonenum();
            String latitude = cafeRepository.findByCafeId(cafeId).getLatitude();
            String longitude = cafeRepository.findByCafeId(cafeId).getLongitude();

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

        User user = userRepository.findById(userInfoInJwt.getUserId().orElsethrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        ));

        if(user.getRole().equals("owner")){
            Long cafeId = cafeRepository.findByUser(user).getId();
            List<Menu> menuList = cafeRepository.findAllByCafeId(cafeId);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴 조회에 성공했습니다.")
                    .data(CafeMenusDto.builder()
                            .menuList(getMenuListDto(menuList))
                            .build())
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOW_EXCEPTION);
        }
    }

    //menuListDto 생성 함수
    public List<MenuListDto> getMenuListDto(List<Menu> menuList){
        List<MenuListDto> menuListDtos = new ArrayList<>();

        for(Menu menu : menuList){
            menuListDtos.add(
                    MenuListDto.builder()
                            .category(menu.getCategory())
                            .menuname(menu.getMenuname())
                            .menuimg(menu.getMenuimg())
                            .menuprice(menu.getMenuprice())
                            .build());
        }
        return menuListDtos;
    }

    // 사장님 카페 홈 정보 수정
    public ResponseEntity<?> modifyCafe(HttpServletRequest httpServletRequest,ModifyCafeRequestDto modifyCafeRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserId().orElsethrow(
                () -> new NoExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        ));

        if(user.getRole().equals("owner")){
            Cafe cafe = Cafe.builder().modifyCafeRequestDto(modifyCafeRequestDto).build();
            cafeRepository.save(cafe);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 홈 수정에 성공했습니다.")
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOW_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 등록
    public ResponseEntity<?> registMenu(HttpServletRequest httpServletRequest, RegistMenuRequestDto registMenuRequestDto) {
        userInfoInJwt.getUserInfo_Jwt(httpServletRequest.getHeader("Authorzation"));

        User user = userRepository.findById(userIndfoInJwt.getUserId().orElsethrow(
                () -> new NoExitUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        ));

        if(user.getRole().equals("owner")){
            Long userId = userInfoInJwt.getUserId_InJWT(httpServletRequest.getHeader("Authorization"));
            User user = userRepository.findByUserId(userId);
            Long cafeId = cafeRepository.findByUser(user).getId();
            Cafe cafe = cafeRepository.findByCafeId(cafeId);

            Menu menu = Menu.builder().registMenuRequestDto(registMenuRequestDto).cafe(cafe).build();

            menuRepository.save(menu);
            return ResponseEntity.ok().body(ResponseDto.builder().
                    result(true).
                    message("메뉴가 정상적으로 등록되었습니다.").
                    build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOW_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 수정
    public ResponseEntity<?> modifyMenu(HttpServletRequest httpServletRequest,Long menuId) {
        userInfoInJwt.getUserInfo_Jwt(httpServletRequest.getHeader("Authorzation"));

        User user = userRepository.findById(userIndfoInJwt.getUserId().orElsethrow(
                () -> new NoExitUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        ));

        if(user.getRole().equals("owner")){
            String category = menuRepository.findByMenuId(menuId).getCategory();
            String menuname = menuRepository.findByMenuId(menuId).getMenuname();
            String menuimg = menuRepository.findByMenuId(menuId).getMenuimg();
            int menuprice = menuRepository.findByMenuId(menuId).getMenuprice();

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("홈 수정에 성공했습니다").data(ModifyMenuDto.builder()
                            .category(category)
                            .menuname(menuname)
                            .menuimg(menuimg)
                            .menuprice(menuprice)
                            .build())
                    .build());
        }else{
            throw new NotAllowException(ErrorCode.NOT_ALLOW_EXCEPTION);
        }
    }

    // 사장님 카페 메뉴 삭제
    public ResponseEntity<?> deleteMenu(HttpServletRequest httpServletRequest,Long menuId) {
        userInfoInJwt.getUserInfo_Jwt(httpServletRequest.getHeader("Authorzation"));

        User user = userRepository.findById(userIndfoInJwt.getUserId().orElsethrow(
                () -> new NoExitUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        ));

        if(user.getRole().equals("owner")){ // 본인 카페인지 확인하는 로직도 필요할 것 / 사장님인데 본인의 카페가 아닌경우
            Menu menu = menuRepository.findByMenuId(menuId);
            menuRepository.delete(menu);
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴가 정상적으로 삭제되었습니다.")
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOW_EXCEPTION);
        }
    }
}