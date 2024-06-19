package com.tecnoinf.gestedu.config;

import com.google.firebase.FirebaseApp;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Configuration
public class TestConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        return mailSender;
    }

    @Bean
    @Primary
    public FirebaseApp firebaseApp() {
        return mock(FirebaseApp.class);
    }
}
