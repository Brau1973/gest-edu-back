package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.TipoUsuario;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.dtos.usuario.CrearUsuarioDTO;
import com.tecnoinf.gestedu.models.Coordinador;
import com.tecnoinf.gestedu.models.Funcionario;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.implementations.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.services.implementations.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/administrador")
@Tag(name = "Administrador", description = "API para operaciones de Administradores")
public class AdministradorController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Qualifier("usuarioRepository")
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

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

    @Operation(summary = "Desactivar cuenta usuario (coordinador/funcionario)")
    @PostMapping("/desactivarUsuario")
    //@PreAuthorize("hasAuthority('ROL_ADMINISTRADOR')")
    public ResponseEntity<?> desactivarUsuario(@RequestBody Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        Usuario user = usuario.get();
        if (user instanceof Coordinador || user instanceof Funcionario) {
            usuarioService.desactivarCuentaUsuario(id);
            return new ResponseEntity<>(new BasicInfoUsuarioDTO(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No puede desactivar este usuario.", HttpStatus.BAD_REQUEST);
        }
    }
}
