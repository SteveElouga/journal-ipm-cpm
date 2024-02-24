package com.ipmcpmjournal.journal.ipmcpm.service.impl;

import com.ipmcpmjournal.journal.ipmcpm.model.Validation;
import com.ipmcpmjournal.journal.ipmcpm.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void envoyer(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@nyobe.com");
        mailMessage.setTo(validation.getUser().getEmail());
        mailMessage.setSubject("Votre code d'activation");

        String texte = String.format(
                "Bonjour %s, <br /> Votre code d'activation est %s, A bientot",
                validation.getUser().getFirstname(),
                validation.getCode()
        );

        mailMessage.setText((texte));

        javaMailSender.send(mailMessage);

        System.out.println("Mail Sent successfully...");

    }



}
