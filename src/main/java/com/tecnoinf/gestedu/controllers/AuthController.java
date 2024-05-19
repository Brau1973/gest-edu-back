package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.services.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@Tag(name = "Auth", description = "API para operaciones de Autenticación")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Login de usuario")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value="Authorization") String token) {
        try {
            // Remove 'Bearer ' from the token
            String jwtToken = token.substring(7);
            this.jwtUtils.listaNegraToken(jwtToken);
            return new ResponseEntity<>("Sesión finalizada.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al cerrar la sesión.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
