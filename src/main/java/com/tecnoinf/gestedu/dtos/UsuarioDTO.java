package com.tecnoinf.gestedu.dtos;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String ci;
    private String nombre;
    private String apellido;
    @Email
    private String email;
    private String password;
    private String telefono;
    private String domicilio;
    private String fechaNac;
    
}
