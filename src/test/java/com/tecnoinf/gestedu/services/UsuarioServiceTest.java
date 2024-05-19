package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByCi() {
        // Arrange
        Coordinador usuario = new Coordinador();
        usuario.setCi("1234567");
        when(usuarioRepository.findByCi(anyString())).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> result = usuarioService.getByCi("1234567");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1234567", result.get().getCi());
        verify(usuarioRepository, times(1)).findByCi("1234567");
    }

    @Test
    void testGetByEmail() {
        // Arrange
        Funcionario usuario = new Funcionario();
        usuario.setEmail("email@example.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> result = usuarioService.getByEmail("email@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("email@example.com", result.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail("email@example.com");
    }

    @Test
    void testUpdateUsuario() {
        // Arrange
        Estudiante usuario = new Estudiante();
        usuario.setCi("1234567");
        when(usuarioRepository.save(any(Estudiante.class))).thenReturn(usuario);

        // Act
        Usuario result = usuarioService.updateUsuario(usuario);

        // Assert
        assertEquals("1234567", result.getCi());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testGetUsuarioDTO() {
        // Arrange
        Coordinador usuario = new Coordinador();
        usuario.setEmail("email@example.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO result = usuarioService.getUsuarioDTO("email@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("email@example.com", result.getEmail());
        verify(usuarioRepository, times(1)).findByEmail("email@example.com");
    }

    @Test
    void testGetUsuarioDTONotFound() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        UsuarioDTO result = usuarioService.getUsuarioDTO("email@example.com");

        // Assert
        assertNull(result);
        verify(usuarioRepository, times(1)).findByEmail("email@example.com");
    }
}
