package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.services.implementations.AsignaturaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @InjectMocks
    private AsignaturaServiceImpl asignaturaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        asignaturaService = new AsignaturaServiceImpl(asignaturaRepository, null, new ModelMapper(), null);
    }

    @Test
    @DisplayName("Should update Asignatura when valid data is provided")
    public void shouldUpdateAsignaturaWhenValidDataIsProvided() {
        // Given
        Long id = 1L;
        CreateAsignaturaDTO createAsignaturaDTO = new CreateAsignaturaDTO();
        createAsignaturaDTO.setNombre("Updated Name");
        createAsignaturaDTO.setDescripcion("Updated Description");
        createAsignaturaDTO.setCreditos(5);

        Asignatura existingAsignatura = new Asignatura();
        existingAsignatura.setNombre("Old Name");
        existingAsignatura.setDescripcion("Old Description");
        existingAsignatura.setCreditos(3);

        Asignatura updatedAsignatura = new Asignatura();
        updatedAsignatura.setNombre(createAsignaturaDTO.getNombre());
        updatedAsignatura.setDescripcion(createAsignaturaDTO.getDescripcion());
        updatedAsignatura.setCreditos(createAsignaturaDTO.getCreditos());

        when(asignaturaRepository.findById(id)).thenReturn(Optional.of(existingAsignatura));
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(updatedAsignatura);

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