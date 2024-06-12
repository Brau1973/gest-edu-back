package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.models.Notificacion;
import java.util.concurrent.ExecutionException;

public interface NotificationService {
    void enviarNotificacion(Notificacion notificacion) throws InterruptedException, ExecutionException;
}
