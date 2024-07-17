package com.tecnoinf.gestedu.dtos.inscripcionCarrera;

import com.tecnoinf.gestedu.dtos.Tramite.UsuarioTramiteInfoDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InscripcionCarreraDTO {
    private Long id;
    private UsuarioTramiteInfoDTO estudiante;;
    Integer creditosObtenidos;
    private EstadoInscripcionCarrera estado;
    private LocalDate fechaInscripcion;
}
