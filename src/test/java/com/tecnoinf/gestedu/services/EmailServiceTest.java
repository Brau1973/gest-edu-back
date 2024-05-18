package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.EmailValuesDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmailResetPass() throws MessagingException {
        // Arrange
        EmailValuesDTO dto = new EmailValuesDTO();
        dto.setMailFrom("from@example.com");
        dto.setMailTo("to@example.com");
        dto.setMailSubject("Password Reset");
        dto.setTokenPassword("token123");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Mock TemplateEngine
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>email content</html>");

        // Act
        emailService.sendEmailResetPass(dto);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(templateEngine, times(1)).process(eq("emailPass"), any(Context.class));
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}
