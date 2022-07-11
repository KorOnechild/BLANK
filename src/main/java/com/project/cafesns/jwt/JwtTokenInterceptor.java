package com.project.cafesns.jwt;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.token.BearerTokenException;
import com.project.cafesns.error.exceptions.token.NullTokenException;
import com.project.cafesns.validator.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String uri = request.getRequestURI();
        String authorization = request.getHeader("Authorization");


        if(authorization == null){
            throw new NullTokenException(ErrorCode.NULL_TOKEN_EXCEPTION);
        }else if(!authorization.startsWith("Bearer ")){
            throw new BearerTokenException(ErrorCode.BEARER_TOKEN_EXCEPTION);
        }else{
            String accessToken = authorization.substring(7);
            if (jwtTokenValidator.isValidAccessToken(accessToken)) {
                return true;
            }
            return false;
        }
    }
}
