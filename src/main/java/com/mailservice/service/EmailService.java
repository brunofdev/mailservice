package com.mailservice.service;

import com.mailservice.dto.UserDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }
    public void sendWelcomeEmail(UserDTO user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("brunofragaa97@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Seja bem-vindo(a), " + user.getNome() + "!");
        message.setText("Olá " + user.getNome() + ",\n\nSeu cadastro foi realizado com sucesso. Estamos felizes em ter você conosco!");
        mailSender.send(message);
        System.out.println("Email de boas-vindas enviado para " + user.getEmail());
    }
}
