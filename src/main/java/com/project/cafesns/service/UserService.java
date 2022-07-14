package com.project.cafesns.service;

import com.project.cafesns.encoder.SHA256;
import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.token.NotExistTokenException;
import com.project.cafesns.error.exceptions.user.EmailDupblicateException;
import com.project.cafesns.error.exceptions.user.NicknameDubplicateException;
import com.project.cafesns.error.exceptions.user.NotmatchUserException;
import com.project.cafesns.jwt.JwtTokenProvider;
import com.project.cafesns.jwt.UserInfoInJwt;
import com.project.cafesns.model.dto.ResponseDto;
import com.project.cafesns.model.dto.user.*;
import com.project.cafesns.model.entitiy.RefreshToken;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.RefreshTokenRepository;
import com.project.cafesns.repository.UserRepository;
import com.project.cafesns.s3.FileUploadService;
import com.project.cafesns.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {
    //aws S3
    private final FileUploadService fileUploadService;

    //validator
    private final UserValidator userValidator;

    //JWT
    private final UserInfoInJwt userInfoInJwt;
    private final JwtTokenProvider jwtTokenProvider;

    //Repository
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    //회원가입
    //TODO : 회원가입시 프로필, 로고 이미지 받는거 처리 따로(해결)
    //TODO : 별로 효율적인 코드같지 않아서 수정할것
    public ResponseEntity<?> signup(MultipartFile file, SignupRequestDto signupRequestDto) throws NoSuchAlgorithmException {

        if(userValidator.checkDubpEmail(signupRequestDto.getEmail())){
            throw new EmailDupblicateException(ErrorCode.EMAIL_DUBP_EXCEPTION);
        }else if(userValidator.checkDubpNickname(signupRequestDto.getNickname())){
            throw new NicknameDubplicateException(ErrorCode.NICKNAME_DUBP_EXCEPTION);
        }else{
            String encodedPw = SHA256.encrypt(signupRequestDto.getPassword());
            if(file.isEmpty()){
                if(signupRequestDto.getRole().equals("user")){
                    User user = User.builder().signupRequestDto(signupRequestDto).encodedPw(encodedPw).build();
                    userRepository.save(user);
                    return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("회원가입에 성공했습니다.").build());
                }else{
                    User user = User.builder().signupRequestDto(signupRequestDto).encodedPw(encodedPw).build();
                    userRepository.save(user);
                    return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("회원가입에 성공했습니다.").build());
                }
            }else{
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

    //로그인
    //TODO : 이후에 아이디, 비밀번호 틀림 예외처리 따로 해줘야 할지도
    @Transactional
    public ResponseEntity<?> signin(SigninReqeustDto signinReqeustDto) throws NoSuchAlgorithmException, RuntimeException {
        String email = signinReqeustDto.getEmail();
        String encodedPW = SHA256.encrypt(signinReqeustDto.getPassword());

        if(!userRepository.existsByPassword(encodedPW) || !userRepository.existsByEmail(email)){
            throw new NotmatchUserException(ErrorCode.NOTMATCH_USER_EXCEPTION);
        }

        User user = userRepository.findByEmail(email);
        //로그인 시 refresh 토큰 존재하면 삭제
        if(refreshTokenRepository.existsByUser(user)){
            RefreshToken refreshTokenCheck = refreshTokenRepository.findByUser(user);
            refreshTokenRepository.delete(refreshTokenCheck);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        RefreshToken refreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenRepository.save(refreshToken);

        SigninDto signinDto = SigninDto.builder()
                .nickname(user.getNickname())
                .role(user.getRole())
                .businessname(user.getBusinessname())
                .profileimg(user.getProfileimg())
                .logoimg(user.getLogoimg())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshtoken())
                .build();

        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("로그인 되었습니다.").data(signinDto).build());
    }

    //로그아웃
    //TODO : 로그아웃 처리를 현재 토큰을 받아서 처리하도록 함 이메일만 받아서 처리해도 되면 변경할 것
    @Transactional
    public ResponseEntity<?> signout(HttpServletRequest httpServletRequest) {
        userInfoInJwt.getUserInfo_InJwt(httpServletRequest.getHeader("Authorization"));


        User user = userRepository.findById(userInfoInJwt.getUserid()).orElseThrow(
                ()-> new NullPointerException("사용자 정보가 없습니다.")
        );

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
        refreshTokenRepository.delete(refreshToken);

        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("로그아웃 되었습니다.").build());
    }

    //액세스 토큰 재발급
    public ResponseEntity<?> reissueAccessToken(ReissueTokenRequestDto reissueTokenRequestDto) throws RuntimeException {
        String refreshToken = reissueTokenRequestDto.getRefreshToken();
        String nickname = reissueTokenRequestDto.getNickname();

        User user = userRepository.findByNickname(nickname);

        if(!userValidator.checkexistRefreshToken(refreshToken, user)){
            throw new NotExistTokenException(ErrorCode.NOTEXIST_TOKEN_EXCEPTION);
            //존재하지 않는 토큰이라는 예외처리
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        ReissueTokenDto reissueTokenDto = ReissueTokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

        return ResponseEntity.ok().body(ResponseDto.builder().result(true).message("액세스 토큰이 재발급되었습니다.").data(reissueTokenDto).build());
    }
}
