package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.user.NotExistUserException;
import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.*;
import com.project.cafesns.model.dto.menu.MenuDto;
import com.project.cafesns.model.dto.menu.MenuListDto;
import com.project.cafesns.model.dto.menu.ModifyMenuDto;
import com.project.cafesns.model.dto.menu.RegistMenuRequestDto;
import com.project.cafesns.model.entitiy.*;
import com.project.cafesns.repository.*;
import com.project.cafesns.result.CalculatorImp;
import com.project.cafesns.s3.FileUploadService;
import com.project.cafesns.validator.OwnerValidator;
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

    //calculator
    private final CalculatorImp calculatorImp;
    //validator
    private final OwnerValidator ownerValidator;

    // 카페 상세 페이지 배너조회
    public ResponseEntity<?> getBanner(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                ()-> new NullPointerException("카페 페이지가 존재하지 않습니다.")
        );

        List<Post> postList = postRepository.findAllByCafe(cafe);
        List<Image> imageList = new ArrayList<>();

        for(int i=0; i < postList.size(); i++){
            if(imageList.size() == 15){break;}
            imageList.addAll(postList.get(i).getImageList());
        }
        
        return ResponseEntity.ok().body(ResponseDto.builder()
                .result(true)
                .message("배너 조회에 성공했습니다.")
                .data(CafeBannerDto.builder()
                        .imageList(imageList)
                        .logoimg(cafe.getUser() == null ? "" : cafe.getUser().getLogoimg())
                        .cafename(cafe.getCafename())
                        .avgstar(calculatorImp.getAvgStar(postList))
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
                .data(getMenuListDto(menuList))
                .build());
    }

    // 사장님 카페 조회
    public ResponseEntity<?> getOwnerCafe(HttpServletRequest httpServletRequest) {

        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );

        Cafe cafe = cafeRepository.findByUser(user);
        String role = userInfoInJwt.getRole();
        ownerValidator.ownerCheck(role);
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

    }

    // 사장님 카페 메뉴 조회
    public ResponseEntity<?> getOwnerCafeMenus(HttpServletRequest httpServletRequest) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

        ownerValidator.ownerShipCheck(user,cafe);
            List<Menu> menuList = menuRepository.findAllByCafe(cafe);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴 조회에 성공했습니다.")
                    .data(getMenuListDto(menuList))
                    .build());
        }

    //menuListDto 생성 함수
    public MenuListDto getMenuListDto(List<Menu> menuList){

        List<MenuDto> drinkList = new ArrayList<>();
        List<MenuDto> dessertList = new ArrayList<>();

        for(Menu menu : menuList){
            if(menu.getCategory().equals("drink")){
                drinkList.add(MenuDto.builder()
                        .menuid(menu.getId())
                        .menuname(menu.getMenuname())
                        .menuimg(menu.getMenuimg())
                        .menuprice(menu.getMenuprice())
                        .build());
            }else{
                dessertList.add(
                        MenuDto.builder()
                                .menuid(menu.getId())
                                .menuname(menu.getMenuname())
                                .menuimg(menu.getMenuimg())
                                .menuprice(menu.getMenuprice())
                                .build());
            }
        }
        return MenuListDto.builder().drink(drinkList).dessert(dessertList).build();
    }

    // 사장님 카페 홈 정보 수정
    //TODO : 변경 안된값은 그대로 보내줌
    @Transactional
    public ResponseEntity<?> modifyCafe(HttpServletRequest httpServletRequest, ModifyCafeRequestDto modifyCafeRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

        ownerValidator.ownerShipCheck(user,cafe);
            cafe.changeCafe(modifyCafeRequestDto);
            cafeRepository.save(cafe);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 홈 수정에 성공했습니다.")
                    .build());
    }

    // 사장님 카페 메뉴 등록
    public ResponseEntity<?> registMenu(HttpServletRequest httpServletRequest, MultipartFile file, RegistMenuRequestDto registMenuRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NotExistUserException(ErrorCode.NOTMATCH_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

        ownerValidator.ownerShipCheck(user,cafe);

        String menuImg = fileUploadService.uploadImage(file, "menu");
        Menu menu = Menu.builder().registMenuRequestDto(registMenuRequestDto).menuimg(menuImg).cafe(cafe).build();
        Long menuid = menuRepository.save(menu).getId();

        return ResponseEntity.ok().body(ResponseDto.builder().
                result(true).
                message("메뉴가 정상적으로 등록되었습니다.").
                data(MenuDto.builder()
                        .category(menu.getCategory())
                        .menuid(menuid)
                        .menuimg(menu.getMenuimg())
                        .menuname(menu.getMenuname())
                        .menuprice(menu.getMenuprice())
                        .build()).
                build());
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
            ownerValidator.ownerShipCheck(user,cafe);
            menu.changeMenu(modifyMenuDto);
            menuRepository.save(menu);
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("메뉴 수정에 성공했습니다")
                    .build());
    }

    // 사장님 카페 메뉴 삭제
    @Transactional
    public ResponseEntity<?> deleteMenu(HttpServletRequest httpServletRequest,Long menuId) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NotExistUserException(ErrorCode.NOTMATCH_USER_EXCEPTION)
        );
        Cafe cafe = cafeRepository.findByUser(user);

       // 본인 카페인지 확인하는 로직도 필요할 것 / 사장님인데 본인의 카페가 아닌경우
            ownerValidator.ownerShipCheck(user,cafe);
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
    }

    //사장님 카페 폐업 기능
    public void deletownecafe(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow( () -> new NullPointerException("해당 유저가 존재하지 않습니다."));
        Cafe cafe = cafeRepository.findByUser(owner);
        cafeRepository.deleteById(cafe.getId());
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
}




