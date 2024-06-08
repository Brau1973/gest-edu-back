package com.tecnoinf.gestedu.dtos.escolaridad;

import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCurso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscolaridadCursoDTO {
    private Long id;
    private LocalDateTime fechaInscripcion;
    private CalificacionCurso calificacion;

    public EscolaridadCursoDTO(InscripcionCurso inscripcionCurso) {
        this.id = inscripcionCurso.getId();
        this.fechaInscripcion = inscripcionCurso.getFechaInscripcion();
        this.calificacion = inscripcionCurso.getCalificacion();
    }
}
