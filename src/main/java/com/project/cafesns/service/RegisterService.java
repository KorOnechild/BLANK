package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.cafe.AlreadyExistCafeException;
import com.project.cafesns.error.exceptions.register.AlreadyExistRegistedException;
import com.project.cafesns.error.exceptions.user.NotExistUserException;
import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.register.RegisterOwnerRequestDto;
import com.project.cafesns.model.dto.register.RegisterRequestDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Register;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.RegisterRepository;
import com.project.cafesns.repository.UserRepository;
import com.project.cafesns.validator.OwnerValidator;
import com.project.cafesns.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;
    private final CafeRepository cafeRepository;
    private final UserInfoInJwt userInfoInJwt;
    private final UserValidator userValidator;
    private final OwnerValidator ownerValidator;

    // 유저일 때 신청
    public ResponseEntity<?> applyCafe(HttpServletRequest httpServletRequest, RegisterRequestDto registerRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );
        String role = userInfoInJwt.getRole();
        userValidator.userCheck(role);
            if(cafeRepository.existsByAddressContainsAndCafename(registerRequestDto.getAddress(), registerRequestDto.getCafename())){
                throw new AlreadyExistCafeException(ErrorCode.ALREADY_EXIST_CAFE_EXCEPTION);
            }else if (registerRepository.existsByAddressAndCafename(registerRequestDto.getAddress(), registerRequestDto.getCafename())){
                throw new AlreadyExistRegistedException(ErrorCode.ALREADY_EXIST_REGISTED_EXCEPTION);
            }else{
                registerRepository.save(Register.builder().registerRequestDto(registerRequestDto).user(user).build()); //register Entity에 userId 넣는가! 아니면 저장을 어떻게 같이 하는지
                return ResponseEntity.ok().body(ResponseDto.builder()
                        .result(true)
                        .message("카페 등록 신청이 완료되었습니다.")
                        .build());
            }
    }

    // 사장님일 때 신청
    public ResponseEntity<?> registCafe(HttpServletRequest httpServletRequest, RegisterOwnerRequestDto registerOwnerRequestDto) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));

        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                () -> new NotExistUserException(ErrorCode.NOT_EXIST_USER_EXCEPTION)
        );
        String role = userInfoInJwt.getRole();
            ownerValidator.ownerCheck(role);
            if(cafeRepository.existsByAddressContainsAndCafename(registerOwnerRequestDto.getAddress(), user.getBusinessname())){
                Cafe cafe = cafeRepository.findByCafename(user.getBusinessname());
                cafe.getOwnership(user);
                cafeRepository.save(cafe);
                return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("이미 존재하는 카페페이지 관리 권한을 얻었습니다.").build());
            }else{
                cafeRepository.save(Cafe.builder().registerOwnerRequestDto(registerOwnerRequestDto).user(user).build()); //register Entity에 userId 넣는가! 아니면 저장을 어떻게 같이 하는지(해결함)
                return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("카페 등록이 완료되었습니다.").build());
            }
    }
}
