package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Actividad;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.ActividadRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.implementations.ActividadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ActividadServiceImplTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ActividadRepository actividadRepository;

    @InjectMocks
    ActividadServiceImpl actividadService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registrarActividad_registersActivitySuccessfully() {
        // Mocking the authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");

        // Mocking the SecurityContextHolder
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        // Mocking the user repository
        Usuario usuario = new Estudiante();
        usuario.setEmail("test@test.com");
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        // Call the method to test
        actividadService.registrarActividad(TipoActividad.INICIAR_SESION, "Test description");

        // Verify that save method was called
        verify(actividadRepository, times(1)).save(any(Actividad.class));
    }

    @Test
    public void registrarActividad_throwsException_whenUserNotFound() {
        // Mocking the authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");

        // Mocking the SecurityContextHolder
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        // Mocking the user repository
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        // Assert that an exception is thrown
        assertThrows(ResourceNotFoundException.class, () -> {
            actividadService.registrarActividad(TipoActividad.INICIAR_SESION, "Test description");
        });
    }

}
