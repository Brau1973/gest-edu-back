package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import jakarta.mail.MessagingException;
import java.util.List;

public interface TramiteService {
    TramiteDTO nuevoTramite(Long carreraId, TipoTramite tipoTramite, String email) throws MessagingException;
    List<TramiteDTO> listarTramitesInscripcionCarreraPendientes();
    TramiteDTO aprobarTramiteInscripcionCarrera(Long tramiteId, String email) throws MessagingException;
    TramiteDTO rechazarTramiteInscripcionCarrera(Long tramiteId, String email, String motivoRechazo) throws MessagingException;
    List<TramiteDTO> listarTramitesSolicitudTituloPendientes();
    TramiteDTO aprobarTramiteSolicitudTitulo(Long tramiteId, String email) throws MessagingException;
    TramiteDTO rechazarTramiteSolicitudTitulo(Long tramiteId, String email, String motivoRechazo) throws MessagingException;
}
