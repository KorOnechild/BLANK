package com.project.cafesns.service.oauth;

import com.project.cafesns.jwt.JwtTokenProvider;
import com.project.cafesns.model.dto.ouath.OauthLoginDto;
import com.project.cafesns.model.dto.ouath.OauthUserInfoDto;
import com.project.cafesns.model.entitiy.RefreshToken;
import com.project.cafesns.model.entitiy.User;
import com.project.cafesns.repository.RefreshTokenRepository;
import com.project.cafesns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;
    // 유저 데이터 기반 로그인 및 회원가입 로직
    public OauthLoginDto oauthlogin(OauthUserInfoDto oauthUserInfoDto, String oauthtype){

        if(oauthUserInfoDto.getEmail() == null){
            oauthUserInfoDto.setEmail(oauthUserInfoDto.getNickname());
        }
        if(userRepository.existsByEmail(oauthUserInfoDto.getEmail())){
            //해당 유저 로그인시키기
            User user = userRepository.findByEmail(oauthUserInfoDto.getEmail());

            if(refreshTokenRepository.existsByUser(user)) {
                RefreshToken refreshTokenCheck = refreshTokenRepository.findByUser(user);
                refreshTokenRepository.delete(refreshTokenCheck);
            }
            return oauthreturn(user, oauthUserInfoDto);
        }
        else {
            //회원가입 + 로그인
            User user = new User(oauthUserInfoDto.getEmail(),oauthUserInfoDto.getNickname(),oauthUserInfoDto.getProfileimg(),oauthtype);
            userRepository.save(user);
            return  oauthreturn(user,oauthUserInfoDto);
        }
    }
    // 로그인 시 필요한 데이터 생성 로직
    public OauthLoginDto oauthreturn (User user, OauthUserInfoDto oauthUserInfoDto){
        String accessToken = jwtTokenProvider.createAccessToken(user);
        RefreshToken refreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenRepository.save(refreshToken);

        OauthLoginDto oauthLoginDto = OauthLoginDto.builder()
                .email(oauthUserInfoDto.getEmail())
                .nickname(oauthUserInfoDto.getNickname())
                .profileimg(oauthUserInfoDto.getProfileimg())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return oauthLoginDto;
    }
}
