package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.TipoUsuario;
import com.tecnoinf.gestedu.dtos.usuario.CrearUsuarioDTO;
import com.tecnoinf.gestedu.services.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administrador")
@Tag(name = "Administrador", description = "API para operaciones de Administradores")
public class AdministradorController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "Registrar un coordinador o funcionario")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRADOR')")
    @PostMapping("/altaUsuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid CrearUsuarioDTO crearUsuarioRequest) {
        TipoUsuario tipo = crearUsuarioRequest.getTipoUsuario();
        if (tipo == TipoUsuario.COORDINADOR || tipo == TipoUsuario.FUNCIONARIO) {
            return new ResponseEntity<>(this.userDetailsService.registrarUsuario(crearUsuarioRequest), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Tipo de usuario no permitido", HttpStatus.BAD_REQUEST);
        }
    }
}
