package com.tecnoinf.gestedu.dtos.inscripcionCurso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionCursoDTO {
    private Long estudianteId;
    private Long cursoId;
}
