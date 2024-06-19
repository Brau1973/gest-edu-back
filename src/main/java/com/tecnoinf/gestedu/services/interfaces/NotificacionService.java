package com.tecnoinf.gestedu.services.interfaces;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tecnoinf.gestedu.dtos.NotificacionDTO;
import com.tecnoinf.gestedu.models.Notificacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NotificacionService {
    void registrarTokenFirebase(String name, String tokenFirebase);
    void enviarNotificacion(Notificacion notificacion, List<String> tokens) throws FirebaseMessagingException;
    void marcarNotificacionLeida(String name, Long idNotificacion);
    List<NotificacionDTO> obtenerNotificaciones(String name);
    Integer obtenerCantidadNotificacionesNoLeidas(String name);
}