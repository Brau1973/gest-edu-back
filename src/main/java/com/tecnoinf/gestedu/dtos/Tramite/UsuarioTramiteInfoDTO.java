package com.tecnoinf.gestedu.dtos.Tramite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioTramiteInfoDTO {
    private Long id;
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
}
