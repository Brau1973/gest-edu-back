package com.tecnoinf.gestedu.dtos.inscripcionCurso;

import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionCursoCalificacionDTO {
    private Long estudianteId;
    private String estudianteNombre;
    private String estudianteApellido;
    private CalificacionCurso calificacionCurso;

    public InscripcionCursoCalificacionDTO(InscripcionCurso inscripcionCurso) {
        this.estudianteId = inscripcionCurso.getEstudiante().getId();
        this.estudianteNombre = inscripcionCurso.getEstudiante().getNombre();
        this.estudianteApellido = inscripcionCurso.getEstudiante().getApellido();
        this.calificacionCurso = inscripcionCurso.getCalificacion();
    }
}
