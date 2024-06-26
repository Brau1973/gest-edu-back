package com.tecnoinf.gestedu.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.curso.ActaCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoEstudianteDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.HorarioRepository;
import com.tecnoinf.gestedu.services.implementations.CursoServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;


public class CursoServiceImplTest {
    @Mock
    CursoRepository cursoRepository;

    @Mock
    AsignaturaRepository asignaturaRepository;

    @Mock
    DocenteRepository docenteRepository;

    @Mock
    HorarioRepository horarioRepository;

    @Mock
    EstudianteRepository estudianteRepository;
    
    @Mock
    ModelMapper modelMapper;

    @Mock
    private ActividadService actividadService;

    @InjectMocks
    CursoServiceImpl cursoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    @Test
    void testCreateCurso_Success_WithValidInput() {
        // Arrange
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setAsignaturaId(1L);
        cursoDTO.setDocenteId(1L);
        cursoDTO.setFechaInicio(LocalDate.now());
        cursoDTO.setFechaFin(LocalDate.now().plusDays(1));
        
        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);

        Docente docente = new Docente();
        docente.setId(1L);

        Curso curso = new Curso();
        curso.setAsignatura(asignatura);
        curso.setDocente(docente);
        curso.setFechaInicio(cursoDTO.getFechaInicio());
        curso.setFechaFin(cursoDTO.getFechaFin());

        when(asignaturaRepository.findById(cursoDTO.getAsignaturaId())).thenReturn(Optional.of(asignatura));
        when(docenteRepository.findById(cursoDTO.getDocenteId())).thenReturn(Optional.of(docente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);
        when(modelMapper.map(any(Curso.class), eq(CursoDTO.class))).thenReturn(cursoDTO);

        // Act
        CursoDTO result = cursoService.createCurso(cursoDTO);

        // Assert
        assertEquals(cursoDTO.getAsignaturaId(), result.getAsignaturaId());
        verify(asignaturaRepository).findById(cursoDTO.getAsignaturaId());
        verify(docenteRepository).findById(cursoDTO.getDocenteId());
        verify(cursoRepository).save(any(Curso.class));
        verify(actividadService).registrarActividad(any(), any());
    }

    @Test
    void testCreateCurso_Failure_AsiganturaNotFound() {
        // Arrange
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setAsignaturaId(1L);
        cursoDTO.setFechaInicio(LocalDate.now());
        cursoDTO.setFechaFin(LocalDate.now().plusDays(1));

        when(asignaturaRepository.findById(cursoDTO.getAsignaturaId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cursoService.createCurso(cursoDTO);
        });
    }

    @Test
    void testGetEstudiantesByCurso_Success() {
        // Arrange
        Long cursoId = 1L;
        Curso curso = new Curso();
        Asignatura asignatura = new Asignatura();
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        InscripcionCurso inscripcion = new InscripcionCurso();
        Estudiante estudiante = new Estudiante();
        inscripcion.setEstudiante(estudiante);
        curso.setInscripciones(List.of(inscripcion));
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        when(modelMapper.map(any(Estudiante.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        // Act
        List<UsuarioDTO> result = cursoService.getEstudiantesByCurso(cursoId);

        // Assert
        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals(1, result.size(), "El tamaño de la lista debería ser 1");
        verify(cursoRepository).findById(cursoId);
        verify(modelMapper).map(any(Estudiante.class), eq(UsuarioDTO.class));
    }

    @Test
    void testGenerarActaCurso_Success() {
        // Arrange
        Long cursoId = 1L;
        Long estudianteId = 1L;
        LocalDate fechaFin = LocalDate.now();

        // Mock Carrera
        Carrera carrera = new Carrera();
        carrera.setId(1L);

        // Mock Asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setCarrera(carrera);

        // Mock Curso
        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        Docente docente = new Docente();
        curso.setDocente(docente);

        // Mock Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteId);
        estudiante.setCi("12345678");
        estudiante.setNombre("Juan");
        estudiante.setApellido("Pérez");
        
        // Mock InscripcionCurso
        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setId(estudianteId);

        inscripcionCurso.setEstudiante(estudiante); // Asignar el estudiante a la inscripción

        curso.setInscripciones(List.of(inscripcionCurso));

        // Mock repository responses
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        when(estudianteRepository.findById(estudianteId)).thenReturn(Optional.of(estudiante));

        // Mock ModelMapper responses
        when(modelMapper.map(any(Asignatura.class), eq(AsignaturaDTO.class))).thenReturn(new AsignaturaDTO());
        when(modelMapper.map(any(Docente.class), eq(DocenteDTO.class))).thenReturn(new DocenteDTO());
        when(modelMapper.map(any(Curso.class), eq(CursoDTO.class))).thenReturn(new CursoDTO());
        when(modelMapper.map(any(InscripcionCurso.class), eq(InscripcionCursoDTO.class))).thenReturn(new InscripcionCursoDTO());
        when(modelMapper.map(any(Usuario.class), eq(BasicInfoEstudianteDTO.class))).thenAnswer(invocation -> {
            Usuario user = invocation.getArgument(0);
            BasicInfoEstudianteDTO dto = new BasicInfoEstudianteDTO();
            dto.setId(user.getId());
            dto.setCi(user.getCi());
            dto.setNombre(user.getNombre());
            dto.setApellido(user.getApellido());
            return dto;
        });

        // Act
        ActaCursoDTO result = cursoService.generarActaCurso(cursoId);

        // Assert
        assertNotNull(result);
        assertEquals(cursoId, result.getId());
        assertEquals(fechaFin, result.getFecha());
        assertNotNull(result.getAsignatura());
        assertNotNull(result.getDocente());
        assertNotNull(result.getCurso());
        assertNotNull(result.getInscripciones());
        assertFalse(result.getInscripciones().isEmpty());
        assertEquals(estudianteId, result.getEstudiantes().get(0).getId());
        assertEquals("12345678", result.getEstudiantes().get(0).getCi());
        assertEquals("Juan", result.getEstudiantes().get(0).getNombre());
        assertEquals("Pérez", result.getEstudiantes().get(0).getApellido());

        verify(cursoRepository).findById(cursoId);
        verify(estudianteRepository).findById(estudianteId);
        verify(actividadService).registrarActividad(eq(TipoActividad.GENERACION_ACTA_CURSO), anyString());
    }

    @Test
    void testGenerarActaCurso_CursoNotFound() {
        // Arrange
        Long cursoId = 1L;

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cursoService.generarActaCurso(cursoId);
        });

        verify(cursoRepository).findById(cursoId);
        verify(actividadService, never()).registrarActividad(any(), any());
    }
}