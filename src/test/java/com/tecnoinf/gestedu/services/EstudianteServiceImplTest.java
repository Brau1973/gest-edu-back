package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EstudianteServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCarrerasNoInscriptoReturnsPageOfCarrerasWhenStudentExists() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        when(estudianteRepository.findByEmail(any(String.class))).thenReturn(Optional.of(estudiante));
        when(carreraRepository.findCarrerasWithPlanEstudioAndEstudianteNotInscripto(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new Carrera())));

        Page<Carrera> result = estudianteService.getCarrerasNoInscripto("test@test.com", PageRequest.of(0, 10));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getCarrerasNoInscriptoThrowsResourceNotFoundExceptionWhenStudentDoesNotExist() {
        when(estudianteRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estudianteService.getCarrerasNoInscripto("test@test.com", PageRequest.of(0, 10)));
    }

    @Test
    public void testObtenerEstudiantePorCiNotFound() {
        String ci = "123456";

        when(usuarioRepository.findByCi(ci)).thenReturn(Optional.empty());

        Optional<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantePorCi(ci);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testObtenerEstudiantes() {
        Estudiante estudiante1 = new Estudiante();
        estudiante1.setCi("123456");
        Estudiante estudiante2 = new Estudiante();
        estudiante2.setCi("789012");

        Pageable pageable = PageRequest.of(0, 2);
        when(usuarioRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(estudiante1, estudiante2), pageable, 2));

        Page<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantes(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("123456", result.getContent().get(0).getCi());
        assertEquals("789012", result.getContent().get(1).getCi());
    }

    @Test
    public void testObtenerEstudiantePorCi() {
        String ci = "123456";
        Estudiante estudiante = new Estudiante();
        estudiante.setCi(ci);

        when(usuarioRepository.findByCi(ci)).thenReturn(Optional.of(estudiante));

        Optional<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantePorCi(ci);

        assertEquals(ci, result.get().getCi());
    }
}