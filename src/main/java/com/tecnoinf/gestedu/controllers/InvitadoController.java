package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.TokenPassDTO;
import com.tecnoinf.gestedu.dtos.TipoUsuario;
import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.dtos.usuario.ChangePasswordDTO;
import com.tecnoinf.gestedu.dtos.usuario.CrearUsuarioDTO;
import com.tecnoinf.gestedu.exceptions.TokenInactivoException;
import com.tecnoinf.gestedu.exceptions.TokenInvalidoException;
import com.tecnoinf.gestedu.exceptions.TokenVencidoException;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.services.implementations.TokenPassService;
import com.tecnoinf.gestedu.services.implementations.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.services.implementations.UsuarioService;
import com.tecnoinf.gestedu.services.interfaces.CertificadoService;
import com.tecnoinf.gestedu.services.interfaces.InvitadoService;
import com.tecnoinf.gestedu.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping()
@Tag(name = "Invitado", description = "API para operaciones de Invitado")
public class InvitadoController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private InvitadoService invitadoService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TokenPassService tokenPassService;

    @Autowired
    CertificadoService certificadoService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Operation(summary = "Login de usuario")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(invitadoService.loginUser(userRequest), HttpStatus.OK);
    }

    @Operation(summary = "Registrar un estudiante")
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrarEstudiante(@RequestBody @Valid CrearUsuarioDTO crearUsuarioRequest) {
        crearUsuarioRequest.setTipoUsuario(TipoUsuario.ESTUDIANTE);
        return new ResponseEntity<>(this.userDetailsService.registrarUsuario(crearUsuarioRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Enviar correo para recuperar contraseña")
    @PostMapping("/correoPassword")
    public ResponseEntity<?> sendEmailResetPassword(@RequestBody TokenPassDTO dto) throws MessagingException {
        Optional<Usuario> usuario = usuarioService.getByEmail(dto.getMailTo());
        if(usuario.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        String token = tokenPassService.crearTokenPassword(usuario.get());
        dto.setTokenPassword(token);
        invitadoService.sendEmailResetPass(dto, usuario.get());
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

        try {
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
        } catch (TokenInvalidoException | TokenInactivoException | TokenVencidoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Validar certificado de estudiante")
    @GetMapping("/validar/{codigoValidacion}")
    public ResponseEntity<CertificadoDTO> validarCertificado(@PathVariable String codigoValidacion) {
        CertificadoDTO certificado = certificadoService.validarCertificado(codigoValidacion);
        return ResponseEntity.ok(certificado);
    }
}
