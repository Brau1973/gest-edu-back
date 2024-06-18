package com.tecnoinf.gestedu.dtos.inscripcionCurso;

import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCurso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionCursoDTO {
    private Long id;
    private LocalDate fechaInscripcion;
    private EstadoInscripcionCurso estado;
    private CalificacionCurso calificacion;
    private Long estudianteId;
    private Long cursoId;

    public InscripcionCursoDTO(InscripcionCurso inscripcion) {
        this.id = inscripcion.getId();
        this.fechaInscripcion = inscripcion.getFechaInscripcion();
        this.estado = inscripcion.getEstado();
        this.calificacion = inscripcion.getCalificacion();
        this.estudianteId = inscripcion.getEstudiante().getId();
        this.cursoId = inscripcion.getCurso().getId();
    }
}
