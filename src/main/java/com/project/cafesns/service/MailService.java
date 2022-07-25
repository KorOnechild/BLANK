package com.project.cafesns.service;

import com.project.cafesns.model.entitiy.Register;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MailService {
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "tmdcks940827@naver.com";

    public void mailSend(Register register) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(register.getUser().getEmail());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("BLANK에서 알려드립니다!");
        message.setText("상기 xxx과 xxxx이유로 귀하의"+register.getCafename()+"의신청이 거절되었습니다");

        mailSender.send(message);
    }
}
