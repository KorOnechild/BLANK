package com.project.cafesns.validator;

import com.project.cafesns.error.ErrorCode;
import com.project.cafesns.error.exceptions.allow.NotAllowedException;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OwnerValidator {
    //사장 유저인지 체크 로직
    public void ownerCheck(String role){
        if(!role.equals("owner")){
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }
    //해당 가게 사장일치로직
    public void ownerShipCheck(User user, Cafe cafe){
        if(!user.getId().equals(cafe.getUser().getId())){
            throw new NotAllowedException(ErrorCode.NOT_ALLOWED_EXCEPTION);
        }
    }
}
