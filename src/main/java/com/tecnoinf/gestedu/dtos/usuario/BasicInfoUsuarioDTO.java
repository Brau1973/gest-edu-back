package com.tecnoinf.gestedu.dtos.usuario;

import com.tecnoinf.gestedu.dtos.TipoUsuario;
import com.tecnoinf.gestedu.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicInfoUsuarioDTO {
    private Long id;
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String domicilio;
    private Date fechaNac;
    private String imagen;
    private TipoUsuario tipoUsuario;

    public BasicInfoUsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.ci = usuario.getCi();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.domicilio = usuario.getDomicilio();
        this.fechaNac = usuario.getFechaNac();
        this.imagen = usuario.getImagen();
        switch (usuario) {
            case Coordinador coordinador -> this.tipoUsuario = TipoUsuario.COORDINADOR;
            case Funcionario funcionario -> this.tipoUsuario = TipoUsuario.FUNCIONARIO;
            case Estudiante estudiante -> this.tipoUsuario = TipoUsuario.ESTUDIANTE;
            case Administrador administrador -> this.tipoUsuario = TipoUsuario.ADMINISTRADOR;
            default -> {
            }
        }
    }
}