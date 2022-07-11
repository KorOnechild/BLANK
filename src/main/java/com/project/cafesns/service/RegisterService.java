package com.project.cafesns.service;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.AlreadyExistCafeOrAddressException;
import com.project.cafesns.error.exceptions.NoExistUserException;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.register.ApplyListResponseDto;
import com.project.cafesns.model.dto.register.RegisterRequestDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Register;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.RegisterRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;
    private final CafeRepository cafeRepository;

    // 유저일 때 신청
    public ResponseEntity<?> applyCafe(HttpServletRequest httpServletRequest, RegisterRequestDto registerRequestDto) {

        Long userId = userInfoInJwt.getUserId_InJWT(httpServletRequest.getHeader("Authorization"));
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new NoExistUserException(ErrorCode.NO_EXIST_USER_EXCEPTION)
        );
        Register register = Register.builder().registerRequestDto(registerRequestDto).user(user).build();

        if(cafeRepository.existsByCafeByAddressAndCafename(registerRequestDto.getAddress(), registerRequestDto.getCafename())){
            throw new AlreadyExistCafenameAndAddressException(ErrorCode.ALREADY_EXIST_CAFE_EXCEPTION);
        }else{
            registerRepository.save(register); //register Entity에 userId 넣는가! 아니면 저장을 어떻게 같이 하는지
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 등록 신청이 완료되었습니다.")
                    .build());
        }
    }

    // 사장님일 때 신청
    public ResponseEntity<?> registCafe(HttpServletRequest httpServletRequest, RegisterRequestDto registerRequestDto) {
        Long userId = userInfoInJwt.getUserId_InJWT(httpServletRequest.getHeader("Authorization"));
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new NoExistUserException(ErrorCode.NO_EXIST_USER_EXCEPTION)
        );
        Cafe cafe = Register.builder().registerRequestDto(registerRequestDto).user(user).build();
        if(cafeRepository.existsByCafeByAddressAndCafename(registerRequestDto.getAddress(), registerRequestDto.getCafename())){
            throw new AlreadyExistCafenameAndAddressException(ErrorCode.ALREADY_EXIST_CAFE_EXCEPTION);
        }else{
            cafeRepository.save(cafe); //register Entity에 userId 넣는가! 아니면 저장을 어떻게 같이 하는지
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .result(true)
                    .message("카페 등록 신청이 완료되었습니다.")
                    .build());
        }
    }

}