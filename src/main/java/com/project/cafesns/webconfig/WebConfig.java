package com.project.cafesns.webconfig;

import com.project.cafesns.jwt.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("Content-Type", "Authorization")
//                .allowCredentials(true);
//    }

    //토큰을 받아야 하는 서비스 설정
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtTokenInterceptor) //로그인이 필요한 서비스 요청시 Interceptor가 그 요청을 가로챔
                //공통
                .addPathPatterns("/api/user/signout") //로그아웃
                .addPathPatterns("/api/auth/refresh") //액세스토큰 재발금
                //일반유저
                .addPathPatterns("api/posts/{postId}") //게시글 수정, 삭제
                .addPathPatterns("api/{cafeId}/posts") //게시글 작성
                .addPathPatterns("/api/user/regist-cafe") //카페 신청
                .addPathPatterns("api/posts/{postId}/comments") //댓글 작성
                .addPathPatterns("api/comments/{commentId}") //댓글 수정 삭제
                //사장유저
                .addPathPatterns("/api/owner/regist-cafe") //카페 등록
                //관리자
                .addPathPatterns("/api/registers/permission") //승인 목록 조회
                .addPathPatterns("/api/registeredcafe") //등록된 모든 카페 조회
                .addPathPatterns("/api/registers") //관리자 미처리 목록 조회
                .addPathPatterns("/api/registers/**"); //관리자 승인목록조회, 거절목록 조회, 미처리내역 승인,거절, 관리자 카페생성 승인, 관리자 승인카페 삭제
    }
}