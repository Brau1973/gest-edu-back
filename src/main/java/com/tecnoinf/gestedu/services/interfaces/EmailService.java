package com.tecnoinf.gestedu.services.interfaces;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    // ---------------------------------------- INSCRIPCION CARRERA ----------------------------------------
    @Async
    void sendNuevoTramiteInscripcionCarreraEmail(String to, String estudianteName, String carreraName) throws MessagingException;

    @Async
    void sendAprobacionTramiteInscripcionCarreraEmail(String to, String estudianteName, String carreraName, String userName) throws MessagingException;

    @Async
    void sendRechazoTramiteInscripcionCarreraEmail(String to, String estudianteName, String carreraName, String userName, String motivoRechazo) throws MessagingException;

    // ---------------------------------------- SOLICITUD TITULO ----------------------------------------
    @Async
    void sendNuevoTramiteTituloCarreraEmail(String to, String estudianteName, String carreraName) throws MessagingException;

    @Async
    void sendAprobacionTramiteTituloCarreraEmail(String to, String estudianteName, String carreraName, String userName) throws MessagingException;

    @Async
    void sendRechazoTramiteTituloCarreraEmail(String to, String estudianteName, String carreraName, String userName, String motivoRechazo) throws MessagingException;
}
