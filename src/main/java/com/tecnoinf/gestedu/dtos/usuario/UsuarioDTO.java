package com.tecnoinf.gestedu.dtos.usuario;

import com.tecnoinf.gestedu.models.Usuario;
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
    private String imagen;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.ci = usuario.getCi();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.domicilio = usuario.getDomicilio();
        if (usuario.getFechaNac() != null) {
            this.fechaNac = usuario.getFechaNac().toString();
        } else {
            this.fechaNac = null;
        }
        this.imagen = usuario.getImagen();
    }
}
