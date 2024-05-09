package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.AuthResponse;
import com.tecnoinf.gestedu.services.UserDetailsServiceImpl;
import com.tecnoinf.gestedu.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

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
