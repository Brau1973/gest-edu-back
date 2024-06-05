package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    // ---------------------------------------  RESETEO CONTRASEÑA -----------------------------------------
    @Async
    void sendResetPasswordEmail(String to, String usuarioName, String link) throws MessagingException;

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

    // ---------------------------------------- CALIFICACIONES ----------------------------------------
    @Async
    void sendCalificacionesExamenEmail(String to, String estudianteName, String carreraName, String asignatura, String calificacion) throws MessagingException;


    //---------------------------------REGISTRAR CALIFICACIONES DE CURSO--------------------------------------
    @Async
    void sendCalificacionCursoEmail(String to, String estudianteName, String asignaturaName, String calificacion) throws MessagingException;
}
