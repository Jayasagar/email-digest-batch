package com.jay.emaildigest.batch.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Value("${email.digest.email.from}")
    private String from;

    @Autowired
    private MailSender mailSender;

    public void sendMailMessage(String to, String body) {
        LOG.info(String.format("Sending email to %s, message %s and from: %s", to, body, from));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Komoot hourly digest!");

        message.setText(body);

        this.mailSender.send(message);
    }
}
