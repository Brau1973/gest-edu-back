package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.exceptions.TokenInactivoException;
import com.tecnoinf.gestedu.exceptions.TokenInvalidoException;
import com.tecnoinf.gestedu.exceptions.TokenVencidoException;
import com.tecnoinf.gestedu.models.Coordinador;
import com.tecnoinf.gestedu.models.TokenPass;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.TokenPassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TokenPassServiceTest {

    @Mock
    private TokenPassRepository tokenPassRepository;

    @InjectMocks
    private TokenPassService tokenPassService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearTokenPassword() {
        // Arrange
        Coordinador usuario = new Coordinador();
        String token = UUID.randomUUID().toString();
        TokenPass tokenPass = new TokenPass();
        tokenPass.setToken(token);
        tokenPass.setVencimiento(new Date(System.currentTimeMillis() + 3600000)); // 1 hour later
        tokenPass.setActivo(true);
        tokenPass.setUsuario(usuario);

        when(tokenPassRepository.save(any(TokenPass.class))).thenReturn(tokenPass);

        // Act
        String result = tokenPassService.crearTokenPassword(usuario);

        // Assert
        assertNotNull(result);
        assertDoesNotThrow(() -> UUID.fromString(result)); // Verifica que el resultado es un UUID válido
        verify(tokenPassRepository, times(1)).save(any(TokenPass.class));
    }

    @Test
    void testValidarToken_Success() {
        // Arrange
        Coordinador usuario = new Coordinador();
        TokenPass tokenPass = new TokenPass();
        tokenPass.setToken("validToken");
        tokenPass.setVencimiento(new Date(System.currentTimeMillis() + 3600000)); // 1 hour later
        tokenPass.setActivo(true);
        tokenPass.setUsuario(usuario);

        when(tokenPassRepository.findByToken(anyString())).thenReturn(Optional.of(tokenPass));

        // Act
        Optional<Usuario> result = tokenPassService.validarToken("validToken");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
        verify(tokenPassRepository, times(1)).findByToken("validToken");
    }

    @Test
    void testValidarToken_TokenInvalido() {
        // Arrange
        when(tokenPassRepository.findByToken(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenInvalidoException.class, () -> {
            tokenPassService.validarToken("invalidToken");
        });
        verify(tokenPassRepository, times(1)).findByToken("invalidToken");
    }

    @Test
    void testValidarToken_TokenInactivo() {
        // Arrange
        TokenPass tokenPass = new TokenPass();
        tokenPass.setToken("inactiveToken");
        tokenPass.setVencimiento(new Date(System.currentTimeMillis() + 3600000)); // 1 hour later
        tokenPass.setActivo(false);

        when(tokenPassRepository.findByToken(anyString())).thenReturn(Optional.of(tokenPass));

        // Act & Assert
        assertThrows(TokenInactivoException.class, () -> {
            tokenPassService.validarToken("inactiveToken");
        });
        verify(tokenPassRepository, times(1)).findByToken("inactiveToken");
    }

    @Test
    void testValidarToken_TokenVencido() {
        // Arrange
        TokenPass tokenPass = new TokenPass();
        tokenPass.setToken("expiredToken");
        tokenPass.setVencimiento(new Date(System.currentTimeMillis() - 3600000)); // 1 hour ago
        tokenPass.setActivo(true);

        when(tokenPassRepository.findByToken(anyString())).thenReturn(Optional.of(tokenPass));

        // Act & Assert
        assertThrows(TokenVencidoException.class, () -> {
            tokenPassService.validarToken("expiredToken");
        });
        verify(tokenPassRepository, times(1)).findByToken("expiredToken");
    }

    @Test
    void testInvalidarToken() {
        // Arrange
        TokenPass tokenPass = new TokenPass();
        tokenPass.setToken("tokenToInvalidate");
        tokenPass.setActivo(true);

        when(tokenPassRepository.findByToken(anyString())).thenReturn(Optional.of(tokenPass));

        // Act
        tokenPassService.invalidarToken("tokenToInvalidate");

        // Assert
        verify(tokenPassRepository, times(1)).findByToken("tokenToInvalidate");
        verify(tokenPassRepository, times(1)).save(tokenPass);
        assertFalse(tokenPass.getActivo());
    }
}
