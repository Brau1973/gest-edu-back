package com.tecnoinf.gestedu.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  DocenteDTO{

    private Long id;
    private String documento;
    private String nombre;
    private String apellido;

}
