package com.tecnoinf.gestedu.dtos.Tramite;

import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TramiteDTO {
    private Long id;
    private EstadoTramite estado;
    private LocalDateTime fechaCreacion;
    private TipoTramite tipo;
    private String nombreCarrera;
    private UsuarioTramiteInfoDTO usuarioSolicitante;
    private UsuarioTramiteInfoDTO usuarioResponsable;
}