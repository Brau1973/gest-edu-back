package com.tecnoinf.gestedu.services.implementations;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.tecnoinf.gestedu.models.Notificacion;
import com.tecnoinf.gestedu.services.interfaces.NotificationService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class NotificacionServiceImpl implements NotificationService {

    public void enviarNotificacion(Notificacion notificacion) throws InterruptedException, ExecutionException {
        Map<String, String> datos = new HashMap<>();
        datos.put("fecha", notificacion.getFecha().toString());
        datos.put("destinatario", notificacion.getEstudiante().getNombre() + " " + notificacion.getEstudiante().getApellido());

        Notification notification = Notification.builder()
                .setTitle(notificacion.getTitulo())
                .setBody(notificacion.getDescripcion())
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(notificacion.getToken())
                .putAllData(datos)
                .build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        System.out.println("Notificacion enviada: " + response);
    }
}
