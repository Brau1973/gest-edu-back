package com.tecnoinf.gestedu.dtos.escolaridad;

import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscolaridadCursoDTO {
    private Long id;
    private String fechaFinCurso;
    private CalificacionCurso calificacion;

    public EscolaridadCursoDTO(InscripcionCurso inscripcionCurso) {
        this.id = inscripcionCurso.getId();
        this.fechaFinCurso = inscripcionCurso.getCurso().getFechaFin().toString();
        this.calificacion = inscripcionCurso.getCalificacion();
    }
}
