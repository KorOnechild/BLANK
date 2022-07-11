package com.project.cafesns.service;

import com.project.cafesns.encoder.SHA256;
import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.EmailDupblicateException;
import com.project.cafesns.error.exceptions.NicknameDubplicateException;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.user.SignupRequestDto;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.RefreshTokenRepository;
import com.project.cafesns.repository.UserRepository;
import com.project.cafesns.s3.FileUploadService;
import com.project.cafesns.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {
    //aws S3
    private final FileUploadService fileUploadService;

    //validator
    private final UserValidator userValidator;


    //Repository
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    //회원가입
    //TODO : 회원가입시 프로필, 로고 이미지 받는거 처리 따로
    public ResponseEntity<?> signup(MultipartFile file, SignupRequestDto signupRequestDto) throws NoSuchAlgorithmException {

        if(userValidator.checkDubpEmail(signupRequestDto.getEmail())){
            throw new EmailDupblicateException(ErrorCode.EMAIL_DUBP_EXCEPTION);
        }else if(userValidator.checkDubpNickname(signupRequestDto.getNickname())){
            throw new NicknameDubplicateException(ErrorCode.NICKNAME_DUBP_EXCEPTION);
        }else{
            String encodedPw = SHA256.encrypt(signupRequestDto.getPassword());
            if(signupRequestDto.getRole().equals("user")){
                String profileimg = fileUploadService.uploadImage(file, "profile");
                User user = User.builder().signupRequestDto(signupRequestDto).encodedPw(encodedPw).profileimg(profileimg).logoimg("").build();
                userRepository.save(user);
                return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("회원가입에 성공했습니다.").build());
            }else{
                String logoimg = fileUploadService.uploadImage(file, "logo");
                User user = User.builder().signupRequestDto(signupRequestDto).encodedPw(encodedPw).profileimg("").logoimg(logoimg).build();
                userRepository.save(user);
                return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("회원가입에 성공했습니다.").build());
            }
        }
    }
}
