package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.CreateInscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.implementations.ExamenServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.services.interfaces.PeriodoExamenService;
import com.tecnoinf.gestedu.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExamenServiceImplTest {

    @InjectMocks
    private ExamenServiceImpl examenService;

    @Mock
    private PeriodoExamenService periodoExamenService;

    @Mock
    private CarreraService carreraService;

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private InscripcionExamenRepository inscripcionExamenRepository;

    @Mock
    private InscripcionCursoRepository inscripcionCursoRepository;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAltaExamen() {
        CreateExamenDTO createExamenDto = new CreateExamenDTO();
        createExamenDto.setAsignaturaId(1L);
        createExamenDto.setFecha(LocalDateTime.now().plusDays(3));
        createExamenDto.setDiasPrevInsc(5);
        Long[] docenteIds = new Long[1];
        docenteIds[0] = 1L;
        createExamenDto.setDocenteIds(docenteIds);

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setCarrera(carrera);
        periodoExamen.setFechaInicio(LocalDateTime.now());
        periodoExamen.setFechaFin(LocalDateTime.now().plusDays(5));

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Docente docente = new Docente();
        docente.setId(1L);

        List<PeriodoExamenDTO> periodosExamen = new ArrayList<>();
        PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO();
        periodoExamenDTO.setId(1L);
        periodoExamenDTO.setFechaInicio(LocalDateTime.now());
        periodoExamenDTO.setFechaFin(LocalDateTime.now().plusDays(5));
        periodosExamen.add(periodoExamenDTO);

        Page<PeriodoExamenDTO> page = new PageImpl<>(periodosExamen);

        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(examenRepository.existsByFechaAndAsignatura(any(LocalDateTime.class), any(Asignatura.class))).thenReturn(false);
        when(carreraService.obtenerPeriodosExamenCarrera(anyLong(), any(Pageable.class))).thenReturn(page);

        examenService.altaExamen(createExamenDto);

        verify(examenRepository, times(1)).save(any(Examen.class));
    }

    @Test
    public void testInscribirseExamen_Exitoso() {
        CreateInscripcionExamenDTO createInscripcionExamenDto = new CreateInscripcionExamenDTO();
        createInscripcionExamenDto.setExamenId(1L);
        createInscripcionExamenDto.setEmail("test@test.com");

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Estudiante usuario = new Estudiante();
        usuario.setEmail("test@test.com");
        usuario.setId(1L);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setAsignatura(asignatura);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setId(1L);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(usuario);
        inscripcionCurso.setCalificacion(CalificacionCurso.AEXAMEN);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setFechaInicio(LocalDateTime.now().plusDays(5));
        periodoExamen.setFechaFin(LocalDateTime.now().plusDays(15));

        Examen examen = new Examen();
        examen.setId(1L);
        examen.setFecha(LocalDateTime.now().plusDays(10));
        examen.setDiasPrevInsc(10);
        examen.setAsignatura(asignatura);

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(1L, 1L)).thenReturn(new ArrayList<>());
        when(inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionCurso));
        when(inscripcionExamenRepository.save(any(InscripcionExamen.class))).thenReturn(new InscripcionExamen());

        InscripcionExamenDTO result = examenService.inscribirseExamen(createInscripcionExamenDto);

        verify(usuarioRepository, atLeastOnce()).findByEmail("test@test.com");
        verify(examenRepository, atLeastOnce()).findById(1L);
        verify(inscripcionExamenRepository, atLeastOnce()).findAllByEstudianteIdAndExamenAsignaturaId(anyLong(), anyLong());
        verify(inscripcionCursoRepository, atLeastOnce()).findByEstudianteIdAndCursoAsignaturaId(anyLong(), anyLong());
        verify(inscripcionExamenRepository, atLeastOnce()).save(any(InscripcionExamen.class));

        assertNotNull(result);
    }

    @Test
    public void testInscribirseExamen_EstudianteAprobado() {
        CreateInscripcionExamenDTO createInscripcionExamenDto = new CreateInscripcionExamenDTO();
        createInscripcionExamenDto.setExamenId(1L);
        createInscripcionExamenDto.setEmail("aprobado@test.com");

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Estudiante usuario = new Estudiante();
        usuario.setEmail("aprobado@test.com");
        usuario.setId(1L);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setAsignatura(asignatura);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setId(1L);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(usuario);
        inscripcionCurso.setCalificacion(CalificacionCurso.EXONERADO);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setFechaInicio(LocalDateTime.now().plusDays(5));
        periodoExamen.setFechaFin(LocalDateTime.now().plusDays(15));

        Examen examen = new Examen();
        examen.setId(1L);
        examen.setFecha(LocalDateTime.now().plusDays(10));
        examen.setDiasPrevInsc(10);
        examen.setAsignatura(asignatura);

        when(usuarioRepository.findByEmail("aprobado@test.com")).thenReturn(Optional.of(usuario));
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(1L, 1L)).thenReturn(new ArrayList<>());
        when(inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionCurso));

        InscripcionExamenException exception = assertThrows(InscripcionExamenException.class, () -> {
            examenService.inscribirseExamen(createInscripcionExamenDto);
        });

        assertEquals("El estudiante ya aprobó la asignatura.", exception.getMessage());

        verify(usuarioRepository, atLeastOnce()).findByEmail("aprobado@test.com");
        verify(examenRepository, atLeastOnce()).findById(1L);
        verify(inscripcionExamenRepository, atLeastOnce()).findAllByEstudianteIdAndExamenAsignaturaId(anyLong(), anyLong());
        verify(inscripcionCursoRepository, atLeastOnce()).findByEstudianteIdAndCursoAsignaturaId(anyLong(), anyLong());
        verify(inscripcionExamenRepository, never()).save(any(InscripcionExamen.class));
    }

    @Test
    public void testInscribirseExamen_EstudianteAprobadoConExamen() {
        CreateInscripcionExamenDTO createInscripcionExamenDto = new CreateInscripcionExamenDTO();
        createInscripcionExamenDto.setExamenId(1L);
        createInscripcionExamenDto.setEmail("aprobadoexamen@test.com");

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Estudiante usuario = new Estudiante();
        usuario.setEmail("aprobadoexamen@test.com");
        usuario.setId(1L); // Asegurarse de que el ID esté configurado

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setAsignatura(asignatura);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setId(1L);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(usuario);
        inscripcionCurso.setCalificacion(CalificacionCurso.AEXAMEN);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setFechaInicio(LocalDateTime.now().plusDays(5));
        periodoExamen.setFechaFin(LocalDateTime.now().plusDays(15));

        Examen examen = new Examen();
        examen.setId(1L);
        examen.setFecha(LocalDateTime.now().plusDays(10));
        examen.setDiasPrevInsc(10);
        examen.setAsignatura(asignatura);

        Examen examen2 = new Examen();
        examen2.setId(2L);
        examen2.setFecha(LocalDateTime.now().minusDays(50));
        examen2.setAsignatura(asignatura);

        InscripcionExamen inscripcionExamen = new InscripcionExamen();
        inscripcionExamen.setId(1L);
        inscripcionExamen.setExamen(examen2);
        inscripcionExamen.setEstudiante(usuario);
        inscripcionExamen.setCalificacion(CalificacionExamen.APROBADO);

        when(usuarioRepository.findByEmail("aprobadoexamen@test.com")).thenReturn(Optional.of(usuario));
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionExamen));
        when(inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionCurso));

        InscripcionExamenException exception = assertThrows(InscripcionExamenException.class, () -> {
            examenService.inscribirseExamen(createInscripcionExamenDto);
        });

        assertEquals("El estudiante ya aprobó la asignatura.", exception.getMessage());

        verify(usuarioRepository, atLeastOnce()).findByEmail("aprobadoexamen@test.com");
        verify(examenRepository, atLeastOnce()).findById(1L);
        verify(inscripcionExamenRepository, atLeastOnce()).findAllByEstudianteIdAndExamenAsignaturaId(anyLong(), anyLong());
        verify(inscripcionCursoRepository, never()).findByEstudianteIdAndCursoAsignaturaId(anyLong(), anyLong());
        verify(inscripcionExamenRepository, never()).save(any(InscripcionExamen.class));
    }

    @Test
    public void testInscribirseExamen_FueraDeFechaInscripcion() {
        // Preparar los datos de prueba
        CreateInscripcionExamenDTO createInscripcionExamenDto = new CreateInscripcionExamenDTO();
        createInscripcionExamenDto.setExamenId(1L);
        createInscripcionExamenDto.setEmail("fueraFecha@test.com");

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Estudiante usuario = new Estudiante();
        usuario.setEmail("fueraFecha@test.com");
        usuario.setId(1L); // Asegurarse de que el ID esté configurado

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setAsignatura(asignatura);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setId(1L);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(usuario);
        inscripcionCurso.setCalificacion(CalificacionCurso.AEXAMEN);

        Examen examen = new Examen();
        examen.setId(1L);
        examen.setFecha(LocalDateTime.now().plusDays(20));
        examen.setDiasPrevInsc(10);
        examen.setAsignatura(asignatura);

        when(usuarioRepository.findByEmail("fueraFecha@test.com")).thenReturn(Optional.of(usuario));
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(1L, 1L)).thenReturn(new ArrayList<>());
        when(inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionCurso));

        PeriodoInscripcionExeption exception = assertThrows(PeriodoInscripcionExeption.class, () -> {
            examenService.inscribirseExamen(createInscripcionExamenDto);
        });

        assertEquals("El examen no está en período de inscripción.", exception.getMessage());

        verify(usuarioRepository, atLeastOnce()).findByEmail("fueraFecha@test.com");
        verify(examenRepository, atLeastOnce()).findById(1L);
        verify(inscripcionExamenRepository, never()).findAllByEstudianteIdAndExamenAsignaturaId(anyLong(), anyLong());
        verify(inscripcionCursoRepository, never()).findByEstudianteIdAndCursoAsignaturaId(anyLong(), anyLong());
        verify(inscripcionExamenRepository, never()).save(any(InscripcionExamen.class));
    }

    @Test
    public void testInscribirseExamen_EstudianteYaInscripto() {
        CreateInscripcionExamenDTO createInscripcionExamenDto = new CreateInscripcionExamenDTO();
        createInscripcionExamenDto.setExamenId(1L);
        createInscripcionExamenDto.setEmail("inscrito@test.com");

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Estudiante usuario = new Estudiante();
        usuario.setEmail("inscrito@test.com");
        usuario.setId(1L);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setAsignatura(asignatura);

        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setId(1L);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setEstudiante(usuario);
        inscripcionCurso.setCalificacion(CalificacionCurso.AEXAMEN);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setFechaInicio(LocalDateTime.now().minusDays(5));
        periodoExamen.setFechaFin(LocalDateTime.now().plusDays(15));

        Examen examen = new Examen();
        examen.setId(1L);
        examen.setFecha(LocalDateTime.now().plusDays(10));
        examen.setDiasPrevInsc(10);
        examen.setAsignatura(asignatura);

        InscripcionExamen inscripcionExamen = new InscripcionExamen();
        inscripcionExamen.setId(1L);
        inscripcionExamen.setExamen(examen);
        inscripcionExamen.setEstudiante(usuario);
        inscripcionExamen.setCalificacion(CalificacionExamen.PENDIENTE);

        List<InscripcionExamen> inscripciones = new ArrayList<>();
        inscripciones.add(inscripcionExamen);
        examen.setInscripciones(inscripciones);

        // Configurar los mocks para devolver los datos de prueba
        when(usuarioRepository.findByEmail("inscrito@test.com")).thenReturn(Optional.of(usuario));
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionExamen));
        when(inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(1L, 1L)).thenReturn(Arrays.asList(inscripcionCurso));

        // Ejecutar el servicio y verificar que lanza la excepción esperada
        InscripcionExamenException exception = assertThrows(InscripcionExamenException.class, () -> {
            examenService.inscribirseExamen(createInscripcionExamenDto);
        });

        assertEquals("El estudiante ya está inscripto en el examen.", exception.getMessage());

        // Verificar que los métodos de los mocks fueron llamados correctamente
        verify(usuarioRepository, atLeastOnce()).findByEmail("inscrito@test.com");
        verify(examenRepository, atLeastOnce()).findById(1L);
        verify(inscripcionExamenRepository, never()).findAllByEstudianteIdAndExamenAsignaturaId(anyLong(), anyLong());
        verify(inscripcionCursoRepository, never()).findByEstudianteIdAndCursoAsignaturaId(anyLong(), anyLong());
        verify(inscripcionExamenRepository, never()).save(any(InscripcionExamen.class));
    }


}