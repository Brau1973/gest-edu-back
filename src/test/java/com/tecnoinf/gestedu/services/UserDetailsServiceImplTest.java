package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.AuthResponse;
import com.tecnoinf.gestedu.dtos.CrearUsuarioRequest;
import com.tecnoinf.gestedu.dtos.TipoUsuario;
import com.tecnoinf.gestedu.models.Administrador;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {
    
    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    PasswordEncoder passwordEncoder;
    
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST registrarUsuario
    @Test
    void testLoadUserByUsername_Success_WithValidInput() {
        // Arrange
        String email = "test@test.com";
        Usuario usuario = new Administrador();
        usuario.setEmail(email);
        usuario.setPassword("password");
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_Failure_WithInvalidInput() {
        // Arrange
        String email = "test@test.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
    @Test
    void testRegistrarUsuario_Success_WithValidInput() {
        // Arrange
        CrearUsuarioRequest crearUsuarioRequest = new CrearUsuarioRequest();
        crearUsuarioRequest.setEmail("test@test.com");
        crearUsuarioRequest.setPassword("password");
        crearUsuarioRequest.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        Usuario usuario = new Administrador();
        usuario.setEmail(crearUsuarioRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(crearUsuarioRequest.getPassword()));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        AuthResponse authResponse = userDetailsService.registrarUsuario(crearUsuarioRequest);

        // Assert
        assertNotNull(authResponse);
    }
    
    
}
