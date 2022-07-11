package com.project.cafesns.error;

import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.error.exceptions.register.AlreadyExistRegistedException;
import com.project.cafesns.error.exceptions.token.BearerTokenException;
import com.project.cafesns.error.exceptions.token.NotExistTokenException;
import com.project.cafesns.error.exceptions.token.NullTokenException;
import com.project.cafesns.error.exceptions.token.ReissueTokenException;
import com.project.cafesns.error.exceptions.cafe.AlreadyExistCafeException;
import com.project.cafesns.error.exceptions.user.EmailDupblicateException;
import com.project.cafesns.error.exceptions.user.NicknameDubplicateException;
import com.project.cafesns.error.exceptions.user.NotExistUserException;
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

    //신청
    @ExceptionHandler(AlreadyExistRegistedException.class)
    public ResponseEntity<?> handleAlreadyExistRegistedException(AlreadyExistRegistedException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("이미 신청된 카페입니다.").build());
    }

    //카페
    @ExceptionHandler(AlreadyExistCafeException.class)
    public ResponseEntity<?> handleAlreadyExistCafeException(AlreadyExistCafeException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("이미 존재하는 카페 페이지 입니다.").build());
    }

    //존재하지 않는 유저
    @ExceptionHandler(NotExistUserException.class)
    public ResponseEntity<?> handleNotExistUserException(NotExistUserException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("존재하지 않는 유저입니다.").build());
    }

    //권한 없음
    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<?> handleNotAdminException(NotAllowedException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("접근 권한이 없습니다.").build());
    }

    //토큰
    @ExceptionHandler(NotExistTokenException.class)
    public ResponseEntity<?> handleNotexistTokenException(NotExistTokenException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("일치하는 토큰이 없습니다.").build());
    }

    @ExceptionHandler(NullTokenException.class)
    public ResponseEntity<?> handleNullTokenException(NullTokenException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("토큰이 비었습니다.").build());
    }

    @ExceptionHandler(BearerTokenException.class)
    public ResponseEntity<?> BearerTokenException(BearerTokenException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("Bearer 토큰 형식이 아닙니다.").build());
    }

    @ExceptionHandler(ReissueTokenException.class)
    public ResponseEntity<?> handleReissueTokenException(ReissueTokenException ex){
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.builder().result(false).message("토큰을 재발급 해주세요.").build());
    }
}
