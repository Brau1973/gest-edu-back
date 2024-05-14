package com.tecnoinf.gestedu.dtos.carrera;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarreraDTO {
    private String nombre;
    private String descripcion;
    private Integer duracionAnios;
    private Integer creditos;
}