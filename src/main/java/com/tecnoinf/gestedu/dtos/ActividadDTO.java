package com.tecnoinf.gestedu.dtos;

import com.tecnoinf.gestedu.models.enums.TipoActividad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActividadDTO {
    private Long id;
    private LocalDateTime fecha;
    private TipoActividad tipoActividad;
    private String descripcion;
    private String usuarioId;
}
