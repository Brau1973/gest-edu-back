package com.tecnoinf.gestedu.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditarUsuarioDTO {
    private String telefono;
    private String domicilio;
    private String imagen;
}
