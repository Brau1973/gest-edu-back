package com.tecnoinf.gestedu.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicInfoEstudianteDTO {
    private Long id;
    private String ci;
    private String nombre;
    private String apellido;
}
