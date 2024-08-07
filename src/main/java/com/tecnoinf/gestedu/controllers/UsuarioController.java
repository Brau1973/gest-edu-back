package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.usuario.*;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.services.implementations.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.services.implementations.UsuarioService;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "API para operaciones de Usuarios")
public class UsuarioController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ActividadService actividadService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value="Authorization") String token) {
        //actividadService.registrarActividad(TipoActividad.CIERRE_SESION, "Se ha cerrado sesion.");
        return new ResponseEntity<>("Sesión cerrada", HttpStatus.OK);
    }

    @Operation(summary = "Obtener la info perfil de usuario logueado")
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> obtenerDatosUsuario(Principal principal) {
        if(principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String email = principal.getName();
        Optional<Usuario> usuario = usuarioService.getByEmail(email);
        if(usuario.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Usuario user = usuario.get();
        return new ResponseEntity<>(new UsuarioDTO(user), HttpStatus.OK);
    }

    @Operation(summary = "Editar perfil de usuario logueado")
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioDTO> editarUsuario(Principal principal, @RequestBody EditarUsuarioDTO UsuarioDTO) {
        if(principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String email = principal.getName();
        Optional<Usuario> usuario = usuarioService.getByEmail(email);
        if(usuario.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Usuario user = usuario.get();
        user.setTelefono(UsuarioDTO.getTelefono());
        user.setDomicilio(UsuarioDTO.getDomicilio());
        user.setImagen(UsuarioDTO.getImagen());
        usuarioService.updateUsuario(user);
        actividadService.registrarActividad(TipoActividad.EDITAR_DATOS_BASICOS_DE_PERFIL_DE_USUARIO, "Se modificó el perfil del usuario.");
        return new ResponseEntity<>(new UsuarioDTO(user), HttpStatus.OK);
    }

    @Operation(summary = "Listar usuarios")
    @PreAuthorize("hasAuthority('ROL_ADMINISTRADOR')")
    @GetMapping("/listar")
    public ResponseEntity<Page<BasicInfoUsuarioDTO>> listarUsuarios(Pageable pageable) {
        return new ResponseEntity<>(usuarioService.getBasicInfoUsuarios(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuario por ci")
    @GetMapping("/buscar/{ci}")
    @PreAuthorize("hasAnyAuthority('ROL_FUNCIONARIO', 'ROL_ADMINISTRADOR', 'ROL_COORDINADOR')")
    public ResponseEntity<BasicInfoUsuarioDTO> buscarEstudiantePorCi(@PathVariable String ci) {
        Optional<BasicInfoUsuarioDTO> usuario = usuarioService.obtenerUsuarioPorCi(ci);
        if(usuario.isPresent()){
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
