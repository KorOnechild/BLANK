package com.project.cafesns.error;

import com.project.cafesns.error.exceptions.EmailDupblicateException;
import com.project.cafesns.error.exceptions.NicknameDubplicateException;
import com.project.cafesns.error.exceptions.NotMatchUserException;
import com.project.cafesns.error.exceptions.NotmatchKeyException;
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
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false));
    }

    //닉네임 중복 예외 핸들러
    @ExceptionHandler(NicknameDubplicateException.class)
    public ResponseEntity<?> handleNicknameDupblicateException(NicknameDubplicateException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false));
    }

    //관리자 회원가입시 잘못된 키값 예외 핸들러
    @ExceptionHandler(NotmatchKeyException.class)
    public ResponseEntity<?> handleNotmatchKeyException(NotmatchKeyException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false));
    }

    @ExceptionHandler(NotMatchUserException.class)
    public ResponseEntity<?> handleNotmatchUserException(NotMatchUserException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false));
    }
}
