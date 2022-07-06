package com.project.cafesns.service;

import com.project.cafesns.model.dto.register.RegisterResponseDto;
import com.project.cafesns.model.entitiy.Register;
import com.project.cafesns.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final RegisterRepository registerRepository;

    //승인목록조회
    public List<RegisterResponseDto> readok() {
        List<Register> registers = registerRepository.findAllByPermit(true);
        List<RegisterResponseDto> dtos = new ArrayList<>();
        for (Register register : registers) {
            RegisterResponseDto dto = new RegisterResponseDto(register);
            dtos.add(dto);
        }
        return dtos;
    }
    //거절목록조회
    public List<RegisterResponseDto> readno() {
        List<Register> registers = registerRepository.findAllByPermit(false);
        List<RegisterResponseDto> dtos = new ArrayList<>();
        for (Register register : registers) {
            RegisterResponseDto dto = new RegisterResponseDto(register);
            dtos.add(dto);
        }
        return dtos;
    }
}
