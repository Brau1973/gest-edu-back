package com.tecnoinf.gestedu.dtos.asignatura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private Integer semestrePlanEstudio;
    private Long carreraId;
}
