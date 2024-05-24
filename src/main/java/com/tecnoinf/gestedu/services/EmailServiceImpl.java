package com.tecnoinf.gestedu.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ---------------------------------------- INSCRIPCION CARRERA ----------------------------------------
    @Override
    @Async
    public void sendNuevoTramiteInscripcionCarreraEmail(String to, String estudianteName, String carreraName) throws MessagingException {
        String subject = "Solicitud de inscripción a carrera";
        String htmlBody = "<h1>Hola, " + estudianteName + "</h1>"
                + "<p>Tu solicitud de inscripción a la carrera " + carreraName + " será procesada a la brevedad por un funcionario/coordinador.<br>" +
                " Saludos,<br>" +
                " Equipo GestEdu</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />";
        sendEmail(to, subject, htmlBody);
    }
    @Override
    @Async
    public void sendAprobacionTramiteInscripcionCarreraEmail(String to, String estudianteName, String carreraName, String userName) throws MessagingException {
        String subject = "Aprobación de inscripción a carrera";
        String htmlBody = "<h1> Felicidades, " + estudianteName + "</h1>"
                + "<p>Tu solicitud de inscripción a la carrera " + carreraName + " fue aprobada por el usuario " + userName + ".<br>" +
                " Saludos,<br>" +
                " Equipo GestEdu</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />";
        sendEmail(to, subject, htmlBody);
    }
    @Override
    @Async
    public void sendRechazoTramiteInscripcionCarreraEmail(String to, String estudianteName, String carreraName, String userName, String motivoRechazo) throws MessagingException {
        String subject = "Rechazo de inscripción a carrera";
        String htmlBody = "<h1>Lo sentimos, " + estudianteName + "</h1>"
                + "<p>Tu solicitud de inscripción a la carrera " + carreraName + " fue rechazada por el usuario "
                + userName + " por el siguiente motivo: " + motivoRechazo + ".<br>"
                + "Por cualquier consulta debes recurrir presencialmente a tu centro de estudio <br>"
                + " Saludos,<br>" +
                " Equipo GestEdu</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />";
        sendEmail(to, subject, htmlBody);
    }

    // ---------------------------------------- SOLICITUD TITULO ----------------------------------------
    @Override
    @Async
    public void sendNuevoTramiteTituloCarreraEmail(String to, String estudianteName, String carreraName) throws MessagingException {
        String subject = "Solicitud de título de carrera";
        String htmlBody = "<h1>Hola, " + estudianteName + "</h1>"
                + "<p>Tu solicitud de título de la carrera " + carreraName + " será procesada a la brevedad por un funcionario/coordinador.<br>" +
                " Saludos,<br>" +
                " Equipo GestEdu</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />";
        sendEmail(to, subject, htmlBody);
    }

    @Override
    @Async
    public void sendAprobacionTramiteTituloCarreraEmail(String to, String estudianteName, String carreraName, String userName) throws MessagingException {
        String subject = "Aprobación de solicitud de título de carrera";
        String htmlBody = "<h1> Felicidades, " + estudianteName + "</h1>"
                + "<p>Tu solicitud de titulo de la carrera" + carreraName + " fue aprobada por el usuario " + userName + ".<br>" +
                " Saludos,<br>" +
                " Equipo GestEdu</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />";
        sendEmail(to, subject, htmlBody);
    }

    @Override
    @Async
    public void sendRechazoTramiteTituloCarreraEmail(String to, String estudianteName, String carreraName, String userName, String motivoRechazo) throws MessagingException {
        String subject = "Rechazo de solicitud de título de carrera";
        String htmlBody = "<h1>Lo sentimos, " + estudianteName + "</h1>"
                + "<p>Tu solicitud de titulo de la carrera " + carreraName + " fue rechazada por el usuario "
                + userName + " por el siguiente motivo: " + motivoRechazo + ".<br>"
                + "Por cualquier consulta debes recurrir presencialmente a tu centro de estudio <br>"
                + " Saludos,<br>" +
                " Equipo GestEdu</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />";
        sendEmail(to, subject, htmlBody);
    }

    @Async
    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = createMimeMessageHelper(message, true);

        String imagePath = "static/images/gest-edu-footer-email.png"; // Adjust the path to your image

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        // Attach image
        ClassPathResource resource = new ClassPathResource(imagePath);
        helper.addInline("image", resource);

        mailSender.send(message);
    }

    protected MimeMessageHelper createMimeMessageHelper(MimeMessage message, boolean multipart) throws MessagingException {
        return new MimeMessageHelper(message, multipart);
    }

}
