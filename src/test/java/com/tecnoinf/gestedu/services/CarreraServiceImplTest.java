package com.tecnoinf.gestedu.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Objects;

class CarreraServiceTest {

    @Mock
    CarreraRepository carreraRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CarreraServiceImpl carreraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCarrera_Success_WithValidInput() {
        // Arrange
        CreateCarreraDTO createCarreraDTO = new CreateCarreraDTO("nombre", "descripcion", 4, 240);
        Carrera carrera = new Carrera();
        carrera.setNombre(createCarreraDTO.getNombre());
        carrera.setDescripcion(createCarreraDTO.getDescripcion());
        carrera.setDuracion(createCarreraDTO.getDuracion());
        carrera.setCreditos(createCarreraDTO.getCreditos());

        when(modelMapper.map(eq(createCarreraDTO), eq(Carrera.class))).thenReturn(carrera);
        when(carreraRepository.save(any(Carrera.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Carrera.class), eq(CreateCarreraDTO.class))).thenReturn(createCarreraDTO);

        // Act
        CreateCarreraDTO result = carreraService.createCarrera(createCarreraDTO);

        // Assert
        assertEquals(createCarreraDTO.getNombre(), result.getDescripcion());
        verify(modelMapper).map(eq(createCarreraDTO), eq(Carrera.class));
        verify(carreraRepository).save(argThat(savedCarrera -> {
            return savedCarrera.getNombre().equals(createCarreraDTO.getNombre())
                    && savedCarrera.getDescripcion().equals(createCarreraDTO.getDescripcion())
                    && Objects.equals(savedCarrera.getDuracion(), createCarreraDTO.getDuracion())
                    && Objects.equals(savedCarrera.getCreditos(), createCarreraDTO.getCreditos());
        }));
    }

    @Test
    void testCreateCarrera_Failure_WithDuplicateName() {
        // Arrange
        CreateCarreraDTO createCarreraDTO = new CreateCarreraDTO("carrera1", "descripcion", 4, 240);
        when(carreraRepository.existsByNombre(createCarreraDTO.getNombre())).thenReturn(true);

        // Act and Assert
        assertThrows(UniqueFieldException.class, () -> {
            carreraService.createCarrera(createCarreraDTO);
        });
    }

}

