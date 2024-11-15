package com.kdm360.bridalweb.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.Email;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.repository.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
 
@Service
public class EmailServiceImpl implements EmailService {
	
    @Autowired 
    private JavaMailSender javaMailSender;
 
    @Autowired
    private SettingsService settingsService;
    
    @Value("${spring.mail.username}") private String sender;
 
    public void sendMail(Email details)
    {
    	Settings settings = settingsService.get(1).get();
    	
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(settings.getSystemName() + " - " + details.getSubject());
            javaMailSender.send(mailMessage);
        }

        catch (Exception e) {
        	e.printStackTrace();
        }
    }
 
    public void sendMailWithAttachment(Email details){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
 
        Settings settings = settingsService.get(1).get();
        
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(settings.getSystemName() + " - " + details.getSubject());
            
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
 
            mimeMessageHelper.addAttachment(
                file.getFilename(), file);

            javaMailSender.send(mimeMessage);
        }

        catch (MessagingException e) {
        	e.printStackTrace();
        }
    }
}
