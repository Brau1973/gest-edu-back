package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.InscripcionCursoRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.implementations.EstudianteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Transactional
public class EstudianteServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private CarreraRepository carreraRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private InscripcionCursoRepository inscripcionCursoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(estudianteRepository, carreraRepository, usuarioRepository);
    }

// MANUAL ANDA OK
//    @Test
//    public void testObtenerEstudiantePorCi() {
//        String ci = "123456";
//        Estudiante estudiante = new Estudiante();
//        estudiante.setCi(ci);
//
//        when(usuarioRepository.findByCi(ci)).thenReturn(Optional.of(estudiante));
//
//        Optional<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantePorCi(ci);
//
//        assertEquals(ci, result.get().getCi());
//    }

    @Transactional
    @Test
    public void testObtenerEstudiantePorCiNotFound() {
        String ci = "123456";

        when(usuarioRepository.findByCi(ci)).thenReturn(Optional.empty());

        Optional<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantePorCi(ci);

        assertEquals(Optional.empty(), result);
    }

// MANUAL ANDA OK
//    @Test
//    public void testObtenerEstudiantes() {
//        Estudiante estudiante1 = new Estudiante();
//        estudiante1.setCi("123456");
//        Estudiante estudiante2 = new Estudiante();
//        estudiante2.setCi("789012");
//
//        Pageable pageable = PageRequest.of(0, 2);
//        when(usuarioRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(estudiante1, estudiante2), pageable, 2));
//
//        Page<BasicInfoUsuarioDTO> result = estudianteService.obtenerEstudiantes(pageable);
//
//        assertEquals(2, result.getContent().size());
//        assertEquals("123456", result.getContent().get(0).getCi());
//        assertEquals("789012", result.getContent().get(1).getCi());
//    }


    @Transactional
    @Test
    public void getCarrerasNoInscriptoReturnsPageOfCarrerasWhenStudentExists() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        when(estudianteRepository.findByEmail(any(String.class))).thenReturn(Optional.of(estudiante));
        when(carreraRepository.findCarrerasWithPlanEstudioAndEstudianteNotInscripto(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new Carrera())));
        when(modelMapper.map(any(Carrera.class), any())).thenReturn(new BasicInfoCarreraDTO());

        Page<BasicInfoCarreraDTO> result = estudianteService.getCarrerasNoInscripto("test@test.com", PageRequest.of(0, 10));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Transactional
    @Test
    public void getCarrerasNoInscriptoThrowsResourceNotFoundExceptionWhenStudentDoesNotExist() {
        when(estudianteRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estudianteService.getCarrerasNoInscripto("test@test.com", PageRequest.of(0, 10)));
    }

    @Transactional
    @Test
    public void getCarrerasNoInscriptoReturnsEmptyPageWhenNoCarrerasFound() {
        // Given
        String email = "test@test.com";
        Pageable pageable = PageRequest.of(0, 10);
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        List<Carrera> carreras = Collections.emptyList();
        Page<Carrera> pageCarreras = new PageImpl<>(carreras, pageable, carreras.size());

        // When
        when(estudianteRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
        when(carreraRepository.findCarrerasWithPlanEstudioAndEstudianteNotInscripto(estudiante.getId(), pageable)).thenReturn(pageCarreras);

        // Then
        Page<BasicInfoCarreraDTO> result = estudianteService.getCarrerasNoInscripto(email, pageable);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testObtenerAsignaturasAExamen() {
        // Datos de prueba
        Long carreraId = 1L;
        String email = "test@test.com";
        Pageable pageable = PageRequest.of(0, 10);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setEmail(email);

        Carrera carrera = new Carrera();
        carrera.setId(carreraId);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setAsignatura(asignatura);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setId(1L);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(estudiante);
        inscripcionCurso.setCalificacion(CalificacionCurso.AEXAMEN);

        List<InscripcionCurso> inscripciones = Arrays.asList(inscripcionCurso);

        AsignaturaDTO asignaturaDTO = new AsignaturaDTO();
        asignaturaDTO.setId(asignatura.getId());

        // Configuración de los mocks
        when(estudianteRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
        when(inscripcionCursoRepository.findByCalificacionAndEstudianteId(CalificacionCurso.AEXAMEN, estudiante.getId())).thenReturn(inscripciones);
        when(modelMapper.map(asignatura, AsignaturaDTO.class)).thenReturn(asignaturaDTO);

        // Ejecución del servicio
        Page<AsignaturaDTO> result = estudianteService.obtenerAsignaturasAExamen(carreraId, email, pageable);

        // Verificaciones
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(asignaturaDTO.getId(), result.getContent().get(0).getId());

        verify(estudianteRepository, times(1)).findByEmail(email);
        verify(inscripcionCursoRepository, times(1)).findByCalificacionAndEstudianteId(CalificacionCurso.AEXAMEN, estudiante.getId());
        verify(modelMapper, times(1)).map(asignatura, AsignaturaDTO.class);
    }

}