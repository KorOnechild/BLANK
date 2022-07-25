package com.project.cafesns.validator;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminValidator {
    // 관리자 체크 로직
    public void adminCheck(String userRole){
        if(!userRole.equals("admin")){
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);//관리자 권한 엑셉션
        }
    }
}
