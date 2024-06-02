package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.examen.ActaExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.CreateInscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.implementations.ExamenServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.services.interfaces.EmailService;
import com.tecnoinf.gestedu.services.interfaces.PeriodoExamenService;
import com.tecnoinf.gestedu.exceptions.*;
import jakarta.mail.MessagingException;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private EmailService emailService;

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

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        CreateExamenDTO createExamenDto = new CreateExamenDTO();
        createExamenDto.setAsignaturaId(1L);
        LocalDateTime fecha = LocalDateTime.now().plusDays(3);
        createExamenDto.setFecha(fecha.format(formatter));
        createExamenDto.setDiasPrevInsc(5);
        Long[] docenteIds = new Long[1];
        docenteIds[0] = 1L;
        createExamenDto.setDocenteIds(docenteIds);

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setCarrera(carrera);
        periodoExamen.setFechaInicio(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        periodoExamen.setFechaFin(LocalDateTime.parse(LocalDateTime.now().plusDays(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Docente docente = new Docente();
        docente.setId(1L);

        List<PeriodoExamenDTO> periodosExamen = new ArrayList<>();
        PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO(periodoExamen);
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

    @Test
    public void testListarExamenesPendientes() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Examen examen = new Examen();
        examen.setId(1L);
        examen.setAsignatura(asignatura);
        examen.setFecha(now.minusDays(1));
        examen.setEstado(Estado.ACTIVO);

        List<Examen> examenes = Collections.singletonList(examen);
        Page<Examen> pageExamenes = new PageImpl<>(examenes, pageable, examenes.size());

        when(examenRepository.findAllByFechaBeforeAndEstado(any(LocalDateTime.class), eq(Estado.ACTIVO), eq(pageable))).thenReturn(pageExamenes);

        Page<ExamenDTO> result = null;
        try {
            result = examenService.listarExamenesPendientes(pageable);
        } catch (Exception e) {
            e.printStackTrace();
            fail("El método lanzó una excepción: " + e.getMessage());
        }

        // Verificar los resultados
        assertNotNull(result, "El resultado no debería ser nulo");
        assertFalse(result.isEmpty(), "El resultado no debería estar vacío");
        assertEquals(1, result.getTotalElements(), "Debería haber un examen pendiente");
        verify(examenRepository, times(1)).findAllByFechaBeforeAndEstado(any(LocalDateTime.class), eq(Estado.ACTIVO), eq(pageable));
    }

    @Test
    public void testListarInscriptosExamen() {
        Long examenId = 1L;

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setAsignatura(asignatura);
        examen.setFecha(LocalDateTime.now().plusDays(5));
        examen.setEstado(Estado.ACTIVO);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);

        InscripcionExamen inscripcion = new InscripcionExamen();
        inscripcion.setId(1L);
        inscripcion.setExamen(examen);
        inscripcion.setEstudiante(estudiante);

        examen.setInscripciones(Collections.singletonList(inscripcion));

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));

        // Ejecutar el método del servicio una vez
        List<InscripcionExamenDTO> result = examenService.listarInscriptosExamen(examenId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(examenRepository, times(1)).findById(examenId);
    }

    @Test
    public void testObtenerCalificaciones() {
        Long examenId = 1L;

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setAsignatura(asignatura);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);

        InscripcionExamen inscripcion = new InscripcionExamen();
        inscripcion.setId(1L);
        inscripcion.setExamen(examen);
        inscripcion.setEstudiante(estudiante);

        examen.setInscripciones(Collections.singletonList(inscripcion));

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));

        List<InscripcionExamenCalificacionDTO> result = examenService.obtenerCalificaciones(examenId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(examenRepository, times(1)).findById(examenId);
    }

    @Test
    public void testRegistrarCalificaciones() throws MessagingException {
        Long examenId = 1L;

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setAsignatura(asignatura);
        examen.setEstado(Estado.ACTIVO);

        Estudiante estudiante1 = new Estudiante();
        estudiante1.setId(1L);

        InscripcionExamen inscripcion1 = new InscripcionExamen();
        inscripcion1.setId(1L);
        inscripcion1.setEstudiante(estudiante1);
        inscripcion1.setExamen(examen);

        List<InscripcionExamen> inscripciones = Collections.singletonList(inscripcion1);
        examen.setInscripciones(inscripciones);

        InscripcionExamenCalificacionDTO calificacionDTO = new InscripcionExamenCalificacionDTO();
        calificacionDTO.setEstudianteId(estudiante1.getId());
        calificacionDTO.setCalificacion(CalificacionExamen.APROBADO);

        List<InscripcionExamenCalificacionDTO> calificaciones = Collections.singletonList(calificacionDTO);

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));
        when(inscripcionExamenRepository.findByEstudianteIdAndExamenId(estudiante1.getId(), examenId)).thenReturn(Optional.of(inscripcion1));

        List<InscripcionExamenCalificacionDTO> result = examenService.registrarCalificaciones(examenId, calificaciones);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(examenRepository, times(1)).findById(examenId);
        verify(inscripcionExamenRepository, times(1)).findByEstudianteIdAndExamenId(estudiante1.getId(), examenId);
        verify(inscripcionExamenRepository, times(1)).save(inscripcion1);
    }

    @Test
    public void testRegistrarCalificaciones_ExamenNoEncontrado() {
        Long examenId = 1L;
        InscripcionExamenCalificacionDTO calificacionDTO = new InscripcionExamenCalificacionDTO();
        calificacionDTO.setEstudianteId(1L);
        calificacionDTO.setCalificacion(CalificacionExamen.APROBADO);

        List<InscripcionExamenCalificacionDTO> calificaciones = Collections.singletonList(calificacionDTO);
        when(examenRepository.findById(examenId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            examenService.registrarCalificaciones(examenId, calificaciones);
        });

        assertEquals("Examen no encontrado.", exception.getMessage());
        verify(examenRepository, times(1)).findById(examenId);
        verify(inscripcionExamenRepository, never()).findByEstudianteIdAndExamenId(anyLong(), anyLong());
    }

    @Test
    public void testDarseDeBajaExamen() {
        Long examenId = 1L;
        String email = "test@example.com";

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setAsignatura(asignatura);
        examen.setFecha(LocalDateTime.now().plusDays(5));
        examen.setEstado(Estado.ACTIVO);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setEmail(email);

        InscripcionExamen inscripcion = new InscripcionExamen();
        inscripcion.setId(1L);
        inscripcion.setExamen(examen);
        inscripcion.setEstudiante(estudiante);

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(estudiante));
        when(inscripcionExamenRepository.findByEstudianteIdAndExamenId(estudiante.getId(), examenId)).thenReturn(Optional.of(inscripcion));

        InscripcionExamenDTO result = examenService.darseDeBajaExamen(examenId, email);

        assertNotNull(result);
        assertEquals(inscripcion.getId(), result.getId());
        verify(inscripcionExamenRepository, times(1)).delete(inscripcion);
    }

    @Test
    public void testDarseDeBajaExamen_ExamenNoEncontrado() {
        Long examenId = 1L;
        String email = "test@example.com";

        when(examenRepository.findById(examenId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            examenService.darseDeBajaExamen(examenId, email);
        });

        assertEquals("Examen no encontrado.", exception.getMessage());
        verify(examenRepository, times(1)).findById(examenId);
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(inscripcionExamenRepository, never()).findByEstudianteIdAndExamenId(anyLong(), anyLong());
    }

    @Test
    public void testDarseDeBajaExamen_BajaExamenException_ExamenPasado() {
        Long examenId = 1L;
        String email = "test@example.com";

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setFecha(LocalDateTime.now().minusDays(1));

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));

        BajaExamenException exception = assertThrows(BajaExamenException.class, () -> {
            examenService.darseDeBajaExamen(examenId, email);
        });

        assertEquals("El examen ya pasó, no puede darse de baja.", exception.getMessage());
        verify(examenRepository, times(1)).findById(examenId);
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(inscripcionExamenRepository, never()).findByEstudianteIdAndExamenId(anyLong(), anyLong());
    }

    @Test
    public void testDarseDeBajaExamen_BajaExamenException_MismoDia() {
        Long examenId = 1L;
        String email = "test@example.com";

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setFecha(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));  // Mismo día

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));

        BajaExamenException exception = assertThrows(BajaExamenException.class, () -> {
            examenService.darseDeBajaExamen(examenId, email);
        });

        assertEquals("No puede darse de baja el mismo día del examen.", exception.getMessage());
        verify(examenRepository, times(1)).findById(examenId);
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(inscripcionExamenRepository, never()).findByEstudianteIdAndExamenId(anyLong(), anyLong());
    }

    @Test
    public void testDarseDeBajaExamen_UsuarioNoEncontrado() {
        Long examenId = 1L;
        String email = "test@example.com";

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setFecha(LocalDateTime.now().plusDays(5));  // Fecha futura

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            examenService.darseDeBajaExamen(examenId, email);
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
        verify(examenRepository, times(1)).findById(examenId);
        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(inscripcionExamenRepository, never()).findByEstudianteIdAndExamenId(anyLong(), anyLong());
    }

    @Test
    public void testGenerarActaExamen() {
        Long examenId = 1L;
        LocalDateTime fecha = LocalDateTime.now();

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);
        asignatura.setNombre("Matemáticas");

        Docente docente = new Docente();
        docente.setId(1L);
        docente.setNombre("Profesor X");

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setNombre("Estudiante Y");

        Examen examen = new Examen();
        examen.setId(examenId);
        examen.setFecha(fecha);
        examen.setAsignatura(asignatura);
        examen.setDocentes(Collections.singletonList(docente));

        InscripcionExamen inscripcion = new InscripcionExamen();
        inscripcion.setId(1L);
        inscripcion.setEstudiante(estudiante);
        inscripcion.setExamen(examen);
        examen.setInscripciones(Collections.singletonList(inscripcion));

        // Verificación de que el examen se ha configurado correctamente
        assertNotNull(examen);
        assertEquals(examenId, examen.getId());

        when(examenRepository.findById(examenId)).thenReturn(Optional.of(examen));

        ActaExamenDTO result = examenService.generarActaExamen(examenId);

        assertNotNull(result);
        assertEquals(examenId, result.getId());
        assertEquals(fecha.toString(), result.getFecha());
        assertEquals("Matemáticas", result.getAsignatura().getNombre());
        assertEquals(1, result.getDocentes().size());
        assertEquals("Profesor X", result.getDocentes().get(0).getNombre());
        assertEquals(1, result.getInscripciones().size());
        assertEquals("Estudiante Y", result.getInscripciones().get(0).getEstudiante().getNombre());

        verify(examenRepository, times(1)).findById(examenId);
    }

    @Test
    public void testGenerarActaExamen_ExamenNoEncontrado() {
        Long examenId = 1L;

        when(examenRepository.findById(examenId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            examenService.generarActaExamen(examenId);
        });

        assertEquals("Examen no encontrado.", exception.getMessage());
        verify(examenRepository, times(1)).findById(examenId);
    }
}