package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.services.implementations.AsignaturaServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private ActividadService actividadService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AsignaturaServiceImpl asignaturaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(actividadService).registrarActividad(any(), any());
        //asignaturaService = new AsignaturaServiceImpl(asignaturaRepository, null, new ModelMapper(), null);
    }

    @Test
    @DisplayName("Should update Asignatura when valid data is provided")
    public void shouldUpdateAsignaturaWhenValidDataIsProvided() {
        // Given
        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Long id = 1L;
        CreateAsignaturaDTO createAsignaturaDTO = new CreateAsignaturaDTO();
        createAsignaturaDTO.setNombre("Updated Name");
        createAsignaturaDTO.setDescripcion("Updated Description");
        createAsignaturaDTO.setCreditos(5);

        Asignatura existingAsignatura = new Asignatura();
        existingAsignatura.setId(1L);
        existingAsignatura.setNombre("Old Name");
        existingAsignatura.setDescripcion("Old Description");
        existingAsignatura.setCreditos(3);
        existingAsignatura.setCarrera(carrera);

        Asignatura updatedAsignatura = new Asignatura();
        updatedAsignatura.setNombre(createAsignaturaDTO.getNombre());
        updatedAsignatura.setDescripcion(createAsignaturaDTO.getDescripcion());
        updatedAsignatura.setCreditos(createAsignaturaDTO.getCreditos());
        updatedAsignatura.setCarrera(carrera);

        AsignaturaDTO asignaturaDTO = new AsignaturaDTO(updatedAsignatura);
        asignaturaDTO.setNombre(createAsignaturaDTO.getNombre());
        asignaturaDTO.setDescripcion(createAsignaturaDTO.getDescripcion());
        asignaturaDTO.setCreditos(createAsignaturaDTO.getCreditos());

        when(asignaturaRepository.findById(id)).thenReturn(Optional.of(existingAsignatura));
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(updatedAsignatura);
        when(modelMapper.map(any(Asignatura.class), eq(AsignaturaDTO.class))).thenReturn(asignaturaDTO);
        doNothing().when(actividadService).registrarActividad(any(), any());

        // When
        var result = asignaturaService.updateAsignatura(id, createAsignaturaDTO);

        // Then
        assertEquals(createAsignaturaDTO.getNombre(), result.getNombre());
        assertEquals(createAsignaturaDTO.getDescripcion(), result.getDescripcion());
        assertEquals(createAsignaturaDTO.getCreditos(), result.getCreditos());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when Asignatura does not exist")
    public void shouldThrowResourceNotFoundExceptionWhenAsignaturaDoesNotExist() {
        // Given
        Long id = 1L;
        CreateAsignaturaDTO createAsignaturaDTO = new CreateAsignaturaDTO();

        when(asignaturaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> asignaturaService.updateAsignatura(id, createAsignaturaDTO));
    }
}