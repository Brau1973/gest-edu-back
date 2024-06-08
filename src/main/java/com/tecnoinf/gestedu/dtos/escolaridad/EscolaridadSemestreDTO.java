package com.tecnoinf.gestedu.dtos.escolaridad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscolaridadSemestreDTO {
    private Integer anio;
    private Integer semestre;
    private List<EscolaridadAsignaturaDTO> asignaturas;
}
