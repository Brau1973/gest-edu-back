package com.tecnoinf.gestedu.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.specifications.CarreraSpecification;
import com.tecnoinf.gestedu.services.implementations.CarreraServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CarreraServiceImplTest {

    @Mock
    CarreraRepository carreraRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    private EstudianteService estudianteService;

    @Mock
    private ActividadService actividadService;

    @InjectMocks
    CarreraServiceImpl carreraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    @Test
    @Transactional
    void testCreateCarrera_Success_WithValidInput() {
        // Arrange
        CreateCarreraDTO createCarreraDTO = new CreateCarreraDTO("nombre", "descripcion");
        Carrera carrera = new Carrera();
        carrera.setNombre(createCarreraDTO.getNombre());
        carrera.setDescripcion(createCarreraDTO.getDescripcion());

        when(modelMapper.map(eq(createCarreraDTO), eq(Carrera.class))).thenReturn(carrera);
        when(carreraRepository.save(any(Carrera.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Carrera.class), eq(CreateCarreraDTO.class))).thenReturn(createCarreraDTO);

        // Act
        CreateCarreraDTO result = carreraService.createCarrera(createCarreraDTO);

        // Assert
        assertEquals(createCarreraDTO.getNombre(), result.getNombre());
        verify(modelMapper).map(eq(createCarreraDTO), eq(Carrera.class));
        verify(carreraRepository).save(argThat(savedCarrera -> {
            return savedCarrera.getNombre().equals(createCarreraDTO.getNombre())
                    && savedCarrera.getDescripcion().equals(createCarreraDTO.getDescripcion());
        }));
    }

    @Test
    @Transactional
    void testCreateCarrera_Failure_WithDuplicateName() {
        // Arrange
        CreateCarreraDTO createCarreraDTO = new CreateCarreraDTO("carrera1", "descripcion");
        when(carreraRepository.existsByNombre(createCarreraDTO.getNombre())).thenReturn(true);

        // Act and Assert
        assertThrows(UniqueFieldException.class, () -> {
            carreraService.createCarrera(createCarreraDTO);
        });
    }

    @Test
    @Transactional
    void testGetAllCarreras_Success_WithValidInput() {
        // Arrange
        String nombre = "nombre";
        Carrera carrera = new Carrera();
        carrera.setNombre(nombre);
        Page<Carrera> page = new PageImpl<>(Collections.singletonList(carrera));
        Pageable pageable = PageRequest.of(0, 5);
        BasicInfoCarreraDTO basicInfoCarreraDTO = new BasicInfoCarreraDTO();
        basicInfoCarreraDTO.setNombre(nombre);

        try (MockedConstruction<CarreraSpecification> mockSpec = Mockito.mockConstruction(CarreraSpecification.class)) {
            when(carreraRepository.findAll(any(CarreraSpecification.class), any(Pageable.class))).thenReturn(page);
            when(modelMapper.map(carrera, BasicInfoCarreraDTO.class)).thenReturn(basicInfoCarreraDTO);

            // Act
            Page<BasicInfoCarreraDTO> result = carreraService.getAllCarreras(pageable, nombre);

            // Assert
            assertEquals(1, result.getContent().size());
            assertEquals(nombre, result.getContent().getFirst().getNombre());
            verify(carreraRepository).findAll(any(CarreraSpecification.class), eq(pageable));
        }
    }

    @Test
    @Transactional
    void testGetCarreraBasicInfoById_Success_WithValidId() {
        // Arrange
        Long id = 1L;
        Carrera carrera = new Carrera();
        carrera.setId(id);
        BasicInfoCarreraDTO basicInfoCarreraDTO = new BasicInfoCarreraDTO();
        basicInfoCarreraDTO.setId(1L);

        when(carreraRepository.findById(id)).thenReturn(Optional.of(carrera));
        when(modelMapper.map(carrera, BasicInfoCarreraDTO.class)).thenReturn(basicInfoCarreraDTO);

        // Act
        BasicInfoCarreraDTO result = carreraService.getCarreraBasicInfoById(id);

        // Assert
        assertEquals(id, result.getId());
    }

    @Test
    @Transactional
    void testGetCarreraBasicInfoById_Failure_WithInvalidId() {
        // Arrange
        Long id = 1L;

        when(carreraRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carreraService.getCarreraBasicInfoById(id);
        });
    }

    @Test
    @Transactional
    void testUpdateCarrera_Failure_WithInvalidId() {
        // Arrange
        Long id = 1L;
        CreateCarreraDTO createCarreraDTO = new CreateCarreraDTO();

        when(carreraRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carreraService.updateCarrera(id, createCarreraDTO);
        });
    }

    @Test
    @Transactional
    void testDeleteCarrera_Success_WithValidId() {
        // Arrange
        Long id = 1L;
        Carrera carrera = new Carrera();
        carrera.setId(id);

        when(carreraRepository.findById(id)).thenReturn(Optional.of(carrera));

        // Act
        carreraService.deleteCarrera(id);

        // Assert
        verify(carreraRepository).delete(carrera);
    }

    @Test
    @Transactional
    void testDeleteCarrera_Failure_WithInvalidId() {
        // Arrange
        Long id = 1L;

        when(carreraRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carreraService.deleteCarrera(id);
        });
    }

    @Test
    void getEstudiantesInscriptosReturnsInscripcionesWhenCarreraExists() {
        Long id = 1L;
        Carrera carrera = new Carrera();
        InscripcionCarrera inscripcion = new InscripcionCarrera();
        carrera.setInscripciones(List.of(inscripcion));
        InscripcionCarreraDTO inscripcionCarreraDTO = new InscripcionCarreraDTO();

        when(carreraRepository.findById(id)).thenReturn(Optional.of(carrera));
        when(modelMapper.map(inscripcion, InscripcionCarreraDTO.class)).thenReturn(inscripcionCarreraDTO);
        when(estudianteService.obtenerCreditosAprobados(any(Estudiante.class), any(Carrera.class))).thenReturn(10);

        List<InscripcionCarreraDTO> result = carreraService.getEstudiantesInscriptos(id);

        assertEquals(1, result.size());
        verify(carreraRepository).findById(id);
        verify(modelMapper).map(inscripcion, InscripcionCarreraDTO.class);
    }

    @Test
    void getEstudiantesInscriptosThrowsExceptionWhenCarreraDoesNotExist() {
        Long id = 1L;

        when(carreraRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            carreraService.getEstudiantesInscriptos(id);
        });
    }

}

