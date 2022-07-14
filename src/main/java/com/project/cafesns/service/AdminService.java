package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.cafe.CafeResponseDto;
import com.project.cafesns.model.dto.register.ApplyListResponseDto;
import com.project.cafesns.model.dto.register.RegistListDto;
import com.project.cafesns.model.dto.register.RegisterResponseDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Post;
import com.project.cafesns.model.entitiy.Register;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.PostRepository;
import com.project.cafesns.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final PostService postService;

    //JWT
    private final UserInfoInJwt userInfoInJwt;

    //repository
    private final RegisterRepository registerRepository;

    private final CafeRepository cafeRepository;

    private final PostRepository postRepository;

    //승인목록조회
    public List<RegisterResponseDto> readok(String userRole) {
        adminCheck(userRole);
        List<Register> registers = registerRepository.findAllByPermit(true);
        List<RegisterResponseDto> dtos = new ArrayList<>();
        for (Register register : registers) {
            RegisterResponseDto dto = new RegisterResponseDto(register);
            dtos.add(dto);
        }
        return dtos;
    }
    //거절목록조회
    public List<RegisterResponseDto> readno(String userRole) {
        adminCheck(userRole);
        List<Register> registers = registerRepository.findAllByPermit(false);
        List<RegisterResponseDto> dtos = new ArrayList<>();
        for (Register register : registers) {
            RegisterResponseDto dto = new RegisterResponseDto(register);
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    //신청 permit 변경부분분
    public void permitset(Long registerId, Boolean permit, String userRole) {
        adminCheck(userRole);
        Register register = registerRepository.findById(registerId).orElseThrow( () -> new NullPointerException("존재하지않는 신청입니다"));

        if(permit){
            register.changePermit(true);
            registerRepository.save(register);
        }
        else {
            register.changePermit(false);
            registerRepository.save(register);
        }
    }

    //관리자 카페생성승인
    public void addcafe(Long registerId, String userRole) {
        adminCheck(userRole);
        Register register = registerRepository.findById(registerId).orElseThrow( () -> new NullPointerException("존재하지않는 신청입니다"));
        if(cafeRepository.existsByAddressAndCafename(register.getAddress(),register.getCafename())){
            //이미존재하는 카페 엑셉션
        } else {
            Cafe cafe = new Cafe(register);
            cafeRepository.save(cafe);
        }
    }

    @Transactional
    // 관리자승인 카페 삭제
    public void deletecafe(Long cafeId, String userRole) {
        adminCheck(userRole);
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow( () -> new NullPointerException("존재하지않는 카페입니다"));
        List<Post> posts = postRepository.findAllByCafe(cafe);
        for(Post post: posts) {
            postService.deleteImage(post); //연관된 게시글 이미지 삭제
        }
        cafeRepository.deleteById(cafeId);
    }

    //등록된 카페 모두 조회
    public List<CafeResponseDto> showcafe(String userRole) {
        adminCheck(userRole);
        List<Cafe>cafeList = cafeRepository.findAll();
        List<CafeResponseDto>adminlist = new ArrayList<>();
        for(Cafe cafe:cafeList){
            CafeResponseDto registeredCafe= new CafeResponseDto(cafe);
            adminlist.add(registeredCafe);
        }
        return adminlist;
    }

    // 관리자 미처리 목록 조회
    public ResponseEntity<?> getApplyList(HttpServletRequest httpServletRequest) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        if(userInfoInJwt.getRole().equals("admin")){
            List<Register> registerList = registerRepository.findAllByPermit(null);
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("미처리 목록 조회에 성공했습니다.")
                    .data(ApplyListResponseDto.builder()
                            .registerList(getRegistListDto(registerList))
                            .build())
                    .build());
        }else{
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }


    // 관리자 체크 로직
    public void adminCheck(String userRole){
        if(!userRole.equals("admin")){
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);//관리자 권한 엑셉션
        }
    }

    //registListDto 생성 함수
    public List<RegistListDto> getRegistListDto(List<Register> registerList){
        List<RegistListDto> registListDtos = new ArrayList<>();

        for(Register register : registerList){
            registListDtos.add(
                    RegistListDto.builder()
                            .registerId(register.getId())
                            .cafename(register.getCafename())
                            .address(register.getAddress())
                            .addressdetail(register.getAddressdetail())
                            .zonenum(register.getZonenum())
                            .permit(register.getPermit())
                            .build());
        }
        return registListDtos;
    }
}
