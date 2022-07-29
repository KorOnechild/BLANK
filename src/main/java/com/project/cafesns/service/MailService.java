package com.project.cafesns.service;

import com.project.cafesns.model.entitiy.Register;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;



@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

//    public void mailSend(Register register) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(register.getUser().getEmail());
//        message.setFrom(MailService.FROM_ADDRESS);
//        message.setSubject("BLANK에서 알려드립니다!");
//        message.setText("상기 xxx과 xxxx이유로 귀하의"+register.getCafename()+"의신청이 거절되었습니다");
//
//        mailSender.send(message);
//    }

    public void sendmail(Register register) throws MessagingException {
        Context context = new Context(); context.setVariable("massage", "상기 xxx과 xxxx이유로 귀하의"+register.getCafename()+"의신청이 거절되었습니다");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.addRecipients(MimeMessage.RecipientType.TO, register.getUser().getEmail());
        mimeMessage.setSubject("BLANK에서 알려드립니다!");
        String name = templateEngine.process("reject", context);
        mimeMessage.setText(name, "utf-8", "html");
        mailSender.send(mimeMessage);
    }
}
