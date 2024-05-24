package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EstudianteServiceImplTest {

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private CarreraRepository carreraRepository;

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
}