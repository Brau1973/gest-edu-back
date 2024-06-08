package com.tecnoinf.gestedu.dtos.escolaridad;

import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscolaridadAsignaturaDTO {
    private Long id;
    private String nombre;
    private Integer creditos;
    private List<EscolaridadCursoDTO> cursos;
    private List<EscolaridadExamenDTO> examenes;
}
