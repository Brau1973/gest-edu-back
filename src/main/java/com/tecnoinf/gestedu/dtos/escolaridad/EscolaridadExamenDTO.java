package com.tecnoinf.gestedu.dtos.escolaridad;

import com.tecnoinf.gestedu.models.InscripcionExamen;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscolaridadExamenDTO {
    private Long id;
    private String fechaExamen;
    private CalificacionExamen calificacion;

    public EscolaridadExamenDTO(InscripcionExamen inscripcionExamen) {
        this.id = inscripcionExamen.getId();
        this.fechaExamen = inscripcionExamen.getExamen().getFecha().toString();
        this.calificacion = inscripcionExamen.getCalificacion();
    }
}
