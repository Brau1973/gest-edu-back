package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import jakarta.mail.MessagingException;

public interface TramiteService {
    TramiteDTO nuevoTramite(Long carreraId, TipoTramite tipoTramite, String email) throws MessagingException;
}
