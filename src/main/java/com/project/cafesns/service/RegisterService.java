package com.project.cafesns.service;

import com.project.cafesns.model.dto.cafe.CafeResponseDto;
import com.project.cafesns.model.dto.register.RegisterResponseDto;
import com.project.cafesns.model.entitiy.Cafe;
import com.project.cafesns.model.entitiy.Register;
import com.project.cafesns.repository.CafeRepository;
import com.project.cafesns.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final RegisterRepository registerRepository;

    private final CafeRepository cafeRepository;

    //승인목록조회
    public List<RegisterResponseDto> readok(String userRole) {
        adminCheck(userRole);
        List<Register> registers = registerRepository.findAllByPermit(true);
        List<RegisterResponseDto> dtos = new ArrayList<>();
        for (Register register : registers) {
            RegisterResponseDto dto = new RegisterResponseDto(register);
            dtos.add(dto);
        }
        return dtos;
    }
    //거절목록조회
    public List<RegisterResponseDto> readno(String userRole) {
        adminCheck(userRole);
        List<Register> registers = registerRepository.findAllByPermit(false);
        List<RegisterResponseDto> dtos = new ArrayList<>();
        for (Register register : registers) {
            RegisterResponseDto dto = new RegisterResponseDto(register);
            dtos.add(dto);
        }
        return dtos;
    }

    //신청 permit 변경부분분
   public void permitset(Long registerId, Boolean permit, String userRole) {
       adminCheck(userRole);
        Register register = registerRepository.findById(registerId).orElseThrow( () -> new NullPointerException("존재하지않는 신청입니다"));

        if(permit){
            register.setPermit(true);
            registerRepository.save(register);
        }
        else {
            register.setPermit(false);
            registerRepository.save(register);
        }
    }

    //관리자 카페생성승인
    public void addcafe(Long registerId, String userRole) {
        adminCheck(userRole);
        Register register = registerRepository.findById(registerId).orElseThrow( () -> new NullPointerException("존재하지않는 신청입니다"));
        if(cafeRepository.existsByAddressAndCafename(register.getAddress(),register.getCafename())){
            //이미존재하는 카페 엑셉션
        } else {
        Cafe cafe = new Cafe(register);
        cafeRepository.save(cafe);}
    }

    // 관리자승인 카페 삭제
    public void deletecafe(Long cafeId, String userRole) {
        adminCheck(userRole);
        cafeRepository.deleteById(cafeId);
    }

    //등록된 카페 모두 조회
    public List<CafeResponseDto> showcafe(String userRole) {
        adminCheck(userRole);
        List<Cafe>cafeList = cafeRepository.findAll();
        List<CafeResponseDto>adminlist = new ArrayList<>();
        for(Cafe cafe:cafeList){
            CafeResponseDto registeredCafe= new CafeResponseDto(cafe);
            adminlist.add(registeredCafe);
        }
        return adminlist;
    }

    // 관리자 체크 로직
    public void adminCheck(String userRole){
        if(!userRole.equals("admin")){
            throw //관리자 권한 엑셉션
        }
    }
}
