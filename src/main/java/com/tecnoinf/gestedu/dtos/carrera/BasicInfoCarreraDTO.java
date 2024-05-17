package com.tecnoinf.gestedu.dtos.carrera;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicInfoCarreraDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionAnios;
    private Integer creditos;
    private Boolean existePlanEstudio;
}
