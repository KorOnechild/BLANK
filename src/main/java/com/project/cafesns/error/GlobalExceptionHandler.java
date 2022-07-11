package com.project.cafesns.error;

import com.project.cafesns.error.exceptions.user.EmailDupblicateException;
import com.project.cafesns.error.exceptions.user.NicknameDubplicateException;
import com.project.cafesns.error.exceptions.user.NotmatchUserException;
import com.project.cafesns.model.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //이메일 중복 예외 핸들러
    @ExceptionHandler(EmailDupblicateException.class)
    public ResponseEntity<?> handleEmailDubplicateException(EmailDupblicateException ex){
//        return new ResponseEntity<>(new RespDto("이메일이 중복되었습니다."), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("이메일이 중복되었습니다.").build());
    }

    //닉네임 중복 예외 핸들러
    @ExceptionHandler(NicknameDubplicateException.class)
    public ResponseEntity<?> handleNicknameDupblicateException(NicknameDubplicateException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("닉네임이 중복되었습니다.").build());
    }

    //로그인 시 일치하지 않는 유저 에러
    @ExceptionHandler(NotmatchUserException.class)
    public ResponseEntity<?> handleNotmatchUserException(NotmatchUserException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("아이디 혹은 비밀번호가 틀렸습니다.").build());
    }
}
