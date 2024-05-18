package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.*;
import com.tecnoinf.gestedu.dtos.usuario.*;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.services.EmailService;
import com.tecnoinf.gestedu.services.TokenPassService;
import com.tecnoinf.gestedu.services.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
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
    EmailService emailService;

    @Autowired
    TokenPassService tokenPassService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Operation(summary = "Registrar un estudiante")
    @PostMapping("/registro") //invitado
    public ResponseEntity<AuthResponse> registrarEstudiante(@RequestBody @Valid CrearUsuarioDTO crearUsuarioRequest) {
        crearUsuarioRequest.setTipoUsuario(TipoUsuario.ESTUDIANTE);
        return new ResponseEntity<>(this.userDetailsService.registrarUsuario(crearUsuarioRequest), HttpStatus.CREATED);
    }

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

    @Operation(summary = "Enviar correo para recuperar contraseña")
    @PostMapping("/resetPassword")
    public ResponseEntity<?> sendEmailResetPassword(@RequestBody EmailValuesDTO dto) {

        Optional<Usuario> usuario = usuarioService.getByEmail(dto.getMailTo());

        if(usuario.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        Usuario user = usuario.get();
        String token = tokenPassService.crearTokenPassword(user);
        dto.setMailFrom(emailFrom);
        dto.setMailTo(user.getEmail());
        dto.setMailSubject("Recuperar de contraseña");
        dto.setTokenPassword(token);
        emailService.sendEmailResetPass(dto);
        return new ResponseEntity<>("Correo enviado con éxito", HttpStatusCode.valueOf(200));
    }

    @Operation(summary = "Cambiar contraseña")
    @PostMapping("/cambiarPassword")
    public ResponseEntity<?> cambiarPassword(@Valid @RequestBody ChangePasswordDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.BAD_REQUEST);
        }
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            return new ResponseEntity<>("Las contraseñas no coinciden", HttpStatus.BAD_REQUEST);
        }

        Optional<Usuario> usuario = tokenPassService.validarToken(dto.getTokenPassword());
        if(usuario.isEmpty()){
            return new ResponseEntity<>("Token no encontrado", HttpStatus.NOT_FOUND);
        }
        Usuario user = usuario.get();
        String newPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(newPassword);
        usuarioService.updateUsuario(user);

        tokenPassService.invalidarToken(dto.getTokenPassword());

        return new ResponseEntity<>("Contraseña actualizada.", HttpStatus.OK);
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
        return new ResponseEntity<>(new UsuarioDTO(user), HttpStatus.OK);
    }
}
