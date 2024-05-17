package com.tecnoinf.gestedu.dtos.asignatura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAsignaturaDTO {
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private Long carreraId;
}
