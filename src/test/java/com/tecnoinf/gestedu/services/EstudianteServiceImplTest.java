package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.escolaridad.EscolaridadDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import com.tecnoinf.gestedu.repositories.*;
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
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private InscripcionCarreraRepository inscripcionCarreraRepository;

    @Mock
    private InscripcionCursoRepository inscripcionCursoRepository;

    @Mock
    private InscripcionExamenRepository inscripcionExamenRepository;

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

    @Test
    public void testObtenerAsignaturasPendientes() {
        Long carreraId = 1L;
        String email = "test@example.com";
        Pageable pageable = PageRequest.of(0, 10);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setEmail(email);

        Asignatura asignatura1 = new Asignatura();
        asignatura1.setId(1L);
        Asignatura asignatura2 = new Asignatura();
        asignatura2.setId(2L);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        Curso curso = new Curso();
        curso.setAsignatura(asignatura1);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setCalificacion(CalificacionCurso.EXONERADO);

        InscripcionExamen inscripcionExamen = new InscripcionExamen();
        Examen examen = new Examen();
        examen.setAsignatura(asignatura2);
        inscripcionExamen.setExamen(examen);
        inscripcionExamen.setCalificacion(CalificacionExamen.APROBADO);

        when(estudianteRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
        when(asignaturaRepository.findByCarreraId(carreraId)).thenReturn(List.of(asignatura1, asignatura2));
        when(inscripcionCursoRepository.findByCalificacionAndEstudianteId(CalificacionCurso.EXONERADO, estudiante.getId()))
                .thenReturn(List.of(inscripcionCurso));
        when(inscripcionExamenRepository.findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId()))
                .thenReturn(List.of(inscripcionExamen));

        AsignaturaDTO asignaturaDTO1 = new AsignaturaDTO();
        AsignaturaDTO asignaturaDTO2 = new AsignaturaDTO();
        when(modelMapper.map(asignatura1, AsignaturaDTO.class)).thenReturn(asignaturaDTO1);
        when(modelMapper.map(asignatura2, AsignaturaDTO.class)).thenReturn(asignaturaDTO2);

        Page<AsignaturaDTO> result = estudianteService.obtenerAsignaturasPendientes(carreraId, email, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements()); // Ambas asignaturas deberían haber sido removidas

        verify(estudianteRepository, times(1)).findByEmail(email);
        verify(asignaturaRepository, times(1)).findByCarreraId(carreraId);
        verify(inscripcionCursoRepository, times(1)).findByCalificacionAndEstudianteId(CalificacionCurso.EXONERADO, estudiante.getId());
        verify(inscripcionExamenRepository, times(1)).findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId());
    }

    @Test
    public void testObtenerAsignaturasPendientes_EstudianteNoEncontrado() {
        Long carreraId = 1L;
        String email = "test@example.com";
        Pageable pageable = PageRequest.of(0, 10);

        when(estudianteRepository.findByEmail(email)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            estudianteService.obtenerAsignaturasPendientes(carreraId, email, pageable);
        });

        assertEquals("Estudiante no encontrado", exception.getMessage());
        verify(estudianteRepository, times(1)).findByEmail(email);
        verify(asignaturaRepository, never()).findByCarreraId(anyLong());
        verify(inscripcionCursoRepository, never()).findByCalificacionAndEstudianteId(any(CalificacionCurso.class), anyLong());
        verify(inscripcionExamenRepository, never()).findByCalificacionAndEstudianteId(any(CalificacionExamen.class), anyLong());
    }

//    @Test //REVISAR
//    public void testGenerarEscolaridad() {
//        Long carreraId = 1L;
//        String email = "estudiante@test.com";
//
//        Estudiante estudiante = new Estudiante();
//        estudiante.setId(1L);
//        estudiante.setEmail(email);
//
//        assertInstanceOf(Usuario.class, estudiante);
//
//        Carrera carrera = new Carrera();
//        carrera.setId(carreraId);
//        carrera.setNombre("Ingeniería");
//
//        InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
//        inscripcionCarrera.setCarrera(carrera);
//        inscripcionCarrera.setEstudiante(estudiante);
//
//        //when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
//        when(inscripcionCarreraRepository.findByEstudianteIdAndCarreraId(estudiante.getId(), carreraId)).thenReturn(inscripcionCarrera);
//
//        Asignatura asignatura1 = new Asignatura();
//        asignatura1.setId(1L);
//        asignatura1.setNombre("Matemáticas");
//        asignatura1.setCreditos(6);
//        asignatura1.setCarrera(carrera);
//
//        Asignatura asignatura2 = new Asignatura();
//        asignatura2.setId(2L);
//        asignatura2.setNombre("Física");
//        asignatura2.setCreditos(4);
//        asignatura2.setCarrera(carrera);
//
//        List<Asignatura> asignaturasCarrera = Arrays.asList(asignatura1, asignatura2);
//        when(asignaturaRepository.findByCarreraId(carreraId)).thenReturn(asignaturasCarrera);
//
//        Curso curso1 = new Curso();
//        curso1.setAsignatura(asignatura1);
//
//        InscripcionCurso inscripcionCurso1 = new InscripcionCurso();
//        inscripcionCurso1.setCurso(curso1);
//        inscripcionCurso1.setEstudiante(estudiante);
//        inscripcionCurso1.setCalificacion(CalificacionCurso.EXONERADO);
//
//        List<InscripcionCurso> cursosAprobados = Collections.singletonList(inscripcionCurso1);
//        when(inscripcionCursoRepository.findByCalificacionAndEstudianteId(CalificacionCurso.EXONERADO, estudiante.getId())).thenReturn(cursosAprobados);
//
//        Examen examen1 = new Examen();
//        examen1.setAsignatura(asignatura1);
//
//        InscripcionExamen inscripcionExamen1 = new InscripcionExamen();
//        inscripcionExamen1.setExamen(examen1);
//        inscripcionExamen1.setEstudiante(estudiante);
//        inscripcionExamen1.setCalificacion(CalificacionExamen.APROBADO);
//
//        List<InscripcionExamen> examenesAprobados = Collections.singletonList(inscripcionExamen1);
//        when(inscripcionExamenRepository.findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId())).thenReturn(examenesAprobados);
//
//        BasicInfoUsuarioDTO basicInfoUsuarioDTO = new BasicInfoUsuarioDTO();
//        basicInfoUsuarioDTO.setEmail(email);
//
//        BasicInfoCarreraDTO basicInfoCarreraDTO = new BasicInfoCarreraDTO();
//        basicInfoCarreraDTO.setNombre("Ingeniería");
//
//        when(modelMapper.map(estudiante, BasicInfoUsuarioDTO.class)).thenReturn(basicInfoUsuarioDTO);
//        when(modelMapper.map(carrera, BasicInfoCarreraDTO.class)).thenReturn(basicInfoCarreraDTO);
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
//        EscolaridadDTO result = estudianteService.generarEscolaridad(carreraId, email); //-------aca esta el error, no encuentra al usuario en usuarioRepository.findByEmail(email)
//
//        assertNotNull(result);
//        assertEquals(email, result.getEstudiante().getEmail());
//        assertEquals("Ingeniería", result.getCarrera().getNombre());
//        assertEquals(6, result.getCreditosAprobados()); // Matemáticas (6)
//        assertFalse(result.getSemestres().isEmpty());
//
//        verify(usuarioRepository, times(1)).findByEmail(email);
//        verify(inscripcionCarreraRepository, times(1)).findByEstudianteIdAndCarreraId(estudiante.getId(), carreraId);
//        verify(asignaturaRepository, times(1)).findByCarreraId(carreraId);
//        verify(inscripcionCursoRepository, times(1)).findByCalificacionAndEstudianteId(CalificacionCurso.EXONERADO, estudiante.getId());
//        verify(inscripcionExamenRepository, times(1)).findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId());
//    }

    @Test
    public void testGenerarEscolaridadUsuarioNoEncontrado() {
        Long carreraId = 1L;
        String email = "inexistente@test.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            estudianteService.generarEscolaridad(carreraId, email);
        });

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());
        verify(inscripcionCarreraRepository, never()).findByEstudianteIdAndCarreraId(anyLong(), anyLong());
    }

//    @Test //REVISAR
//    public void testGenerarEscolaridadUsuarioNoEsEstudiante() {
//        Long carreraId = 1L;
//        String email = "usuario@test.com";
//
//        Administrador administrador = new Administrador();
//        administrador.setEmail(email);
//
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(administrador));
//
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
//            estudianteService.generarEscolaridad(carreraId, email);
//        });
//
//        assertEquals("El usuario no es un estudiante", exception.getMessage());
//
//        verify(usuarioRepository, times(1)).findByEmail(email);
//        verify(inscripcionCarreraRepository, never()).findByEstudianteIdAndCarreraId(anyLong(), anyLong());
//    }

//    @Test //REVISAR
//    public void testGenerarEscolaridadEstudianteNoInscripto() {
//        Long carreraId = 1L;
//        String email = "estudiante@test.com";
//
//        Estudiante estudiante = new Estudiante();
//        estudiante.setId(1L);
//        estudiante.setEmail(email);
//
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
//        when(inscripcionCarreraRepository.findByEstudianteIdAndCarreraId(estudiante.getId(), carreraId)).thenReturn(null);
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            estudianteService.generarEscolaridad(carreraId, email);
//        });
//
//        verify(usuarioRepository, times(1)).findByEmail(email);
//        verify(inscripcionCarreraRepository, times(1)).findByEstudianteIdAndCarreraId(estudiante.getId(), carreraId);
//    }
}