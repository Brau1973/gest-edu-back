package com.tecnoinf.gestedu.dtos.inscripcionExamen;

import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionExamenCalificacionDTO {
    private Long estudianteId;
    private String estudianteNombre;
    private String estudianteApellido;
    private CalificacionExamen calificacion;

    public InscripcionExamenCalificacionDTO(InscripcionExamen inscripcionExamen) {
        this.estudianteId = inscripcionExamen.getEstudiante().getId();
        this.estudianteNombre = inscripcionExamen.getEstudiante().getNombre();
        this.estudianteApellido = inscripcionExamen.getEstudiante().getApellido();
        this.calificacion = inscripcionExamen.getCalificacion();
    }
}
