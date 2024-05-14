package com.tecnoinf.gestedu.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.specifications.CarreraSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

class CarreraServiceImplTest {

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
        assertEquals(createCarreraDTO.getNombre(), result.getNombre());
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

    @Test
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
    void testUpdateCarrera_Failure_WithInvalidId() {
        // Arrange
        Long id = 1L;
        BasicInfoCarreraDTO basicInfoCarreraDTO = new BasicInfoCarreraDTO();

        when(carreraRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carreraService.updateCarrera(id, basicInfoCarreraDTO);
        });
    }

    @Test
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
    void testDeleteCarrera_Failure_WithInvalidId() {
        // Arrange
        Long id = 1L;

        when(carreraRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carreraService.deleteCarrera(id);
        });
    }

}

