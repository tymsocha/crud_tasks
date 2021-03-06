package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class SimpleEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailCreatorService mailCreatorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

     public void send(final Mail mail) {
         LOGGER.info("Starting e-mail preparation...");
         try {
             javaMailSender.send(createMimeMessage(mail));
             LOGGER.info("Email has been sent");
         } catch (MailException e) {
             LOGGER.error("Failed to process email sending: " + e.getMessage() + e);
         }
     }

    public void sendScheduledEmail(final Mail mail) {
        LOGGER.info("Starting e-mail preparation...");
        try {
            javaMailSender.send(createScheduledMessage(mail));
            LOGGER.info("Email has been sent");
        } catch (MailException e) {
            LOGGER.error("Failed to process email sending: " + e.getMessage() + e);
        }
    }

    public void sendSimpleEmail(final Mail mail) {
        LOGGER.info("Starting e-mail preparation...");
        try {
            javaMailSender.send(createMailMessage(mail));
            LOGGER.info("Email has been sent");
        } catch (MailException e) {
            LOGGER.error("Failed to process email sending: " + e.getMessage() + e);
        }
    }

    private SimpleMailMessage createMailMessage(Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        if (mail.getToCc() == null || mail.getToCc().equals("")) {
            LOGGER.info("Additional receiver has not been added");
        } else mailMessage.setCc(mail.getToCc());
        return mailMessage;
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail) {
         return mimeMessage -> {
             MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
             messageHelper.setTo(mail.getMailTo());
             messageHelper.setSubject(mail.getSubject());
             messageHelper.setText(mailCreatorService.buildTrelloCardEmail(mail.getMessage()), true);
         };
    }

    private MimeMessagePreparator createScheduledMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mailCreatorService.buildScheduledEmail(mail.getMessage()), true);
        };
    }

}
