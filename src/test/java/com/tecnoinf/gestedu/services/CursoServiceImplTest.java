package com.tecnoinf.gestedu.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.curso.ActaCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Horario;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
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
        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setFechaFin(LocalDate.now());
        Asignatura asignatura = new Asignatura();
        Carrera carrera = new Carrera();
        asignatura.setCarrera(carrera);
        List<Curso> cursos = new ArrayList<>();
        cursos.add(curso);
        asignatura.setCursos(cursos);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        Docente docente = new Docente();
        curso.setDocente(docente);
        InscripcionCurso inscripcion = new InscripcionCurso();
        Estudiante estudiante = new Estudiante();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setCurso(curso);
        curso.setInscripciones(List.of(inscripcion));
        

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        when(modelMapper.map(any(Asignatura.class), eq(AsignaturaDTO.class))).thenReturn(new AsignaturaDTO());
        when(modelMapper.map(any(Docente.class), eq(DocenteDTO.class))).thenReturn(new DocenteDTO());
        when(modelMapper.map(any(Curso.class), eq(CursoDTO.class))).thenReturn(new CursoDTO());
        when(modelMapper.map(any(InscripcionCurso.class), eq(InscripcionCursoDTO.class))).thenReturn(new InscripcionCursoDTO());

        // Act
        ActaCursoDTO result = cursoService.generarActaCurso(cursoId);

        // Assert
        assertNotNull(result);
        assertEquals(cursoId, result.getId());
        verify(cursoRepository).findById(cursoId);
        verify(actividadService).registrarActividad(any(), any());
    }


    @Test
    void testGenerarActaCurso_Failure_CursoNotFound() {
        // Arrange
        Long cursoId = 1L;

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cursoService.generarActaCurso(cursoId);
        });
    }
}

