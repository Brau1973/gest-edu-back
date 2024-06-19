package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnoinf.gestedu.config.TestConfig;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.*;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=update"
})
@AutoConfigureMockMvc
public class InscripcionCursoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired InscripcionCursoRepository inscripcionCursoRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionCarreraRepository inscripcionCarreraRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @MockBean
    private ActividadService actividadService;

    @InjectMocks
    private InscripcionCursoController inscripcionCursoController;

    @BeforeEach
    public void setup() {
        // Configurar el mock para que el método registrarActividad no haga nada
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    @Test
    @WithMockUser(username = "estudiante@gmail.com", roles = { "FUNCIONARIO" })
    @Transactional
    public void registerCurso()  throws Exception  {
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("estudiante@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        //InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
        carrera.setInscripciones(insCarreras);
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        curso = cursoRepository.save(curso);

        //Crear Inscripcion Curso
        InscripcionCursoDTO inscripcionCursoDTO = new InscripcionCursoDTO();
        inscripcionCursoDTO.setEstudianteId(estudiante.getId());
        inscripcionCursoDTO.setCursoId(curso.getId());

        mockMvc.perform(post("/inscripcionCurso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionCursoDTO)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.cursoId").value(curso.getId()))
                .andExpect(jsonPath("$.estudianteId").value(estudiante.getId()))
                .andExpect(jsonPath("$.fechaInscripcion").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.calificacion").value("PENDIENTE"));
    }

    @Test
    @Transactional
    void registrarCalificaciones() throws Exception{
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("gcanepa28@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        //InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
        carrera.setInscripciones(insCarreras);
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        //curso = cursoRepository.save(curso);

        //Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setFechaInscripcion(LocalDate.now());
        inscripcionCurso.setEstudiante(estudiante);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
        inscripcionCurso = inscripcionCursoRepository.save(inscripcionCurso);

        curso.getInscripciones().add(inscripcionCurso);
        curso = cursoRepository.save(curso);


        InscripcionCursoCalificacionDTO calificacion = new InscripcionCursoCalificacionDTO();
        calificacion.setEstudianteId(estudiante.getId());
        calificacion.setEstudianteNombre(estudiante.getNombre());
        calificacion.setEstudianteApellido(estudiante.getApellido());
        calificacion.setCalificacionCurso(CalificacionCurso.EXONERADO);

        List<InscripcionCursoCalificacionDTO> inscripcionesCalificadas = new ArrayList<>();
        inscripcionesCalificadas.add(calificacion);

        System.out.println(estudiante.toString() + calificacion.getEstudianteId());

        mockMvc.perform(put("/inscripcionCurso/" + curso.getId() + "/calificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionesCalificadas)))
                .andExpect(status().isOk());

        // Verificar que la calificación fue actualizada
        InscripcionCurso updatedInscripcionCurso = inscripcionCursoRepository.findById(inscripcionCurso.getId()).orElseThrow();
        assertEquals(CalificacionCurso.EXONERADO, updatedInscripcionCurso.getCalificacion());
    }

    @Test
    @Transactional
    public void cancelInscripcionCurso() throws Exception {
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("estudiante@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        //InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
        carrera.setInscripciones(insCarreras);
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2024, 8, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(90);
        //curso = cursoRepository.save(curso);

        //Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setFechaInscripcion(LocalDate.now());
        inscripcionCurso.setEstudiante(estudiante);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
        inscripcionCurso = inscripcionCursoRepository.save(inscripcionCurso);

        curso.getInscripciones().add(inscripcionCurso);
        curso = cursoRepository.save(curso);

        mockMvc.perform(delete("/inscripcionCurso/cancelarInscripcion/" + inscripcionCurso.getId()))
                .andExpect(status().isNoContent());

        // Verificar que la inscripción fue eliminada
        boolean exists = inscripcionCursoRepository.existsById(inscripcionCurso.getId());
        assert !exists;
    }

    @Test
    @WithMockUser(username = "estudiante@gmail.com", roles = { "ESTUDIANTE" })
    @Transactional
    public void getCursosHorariosInscriptos() throws Exception {
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("estudiante@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);
    System.out.println(usuarioRepository.findById(estudiante.getId()));
        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);
    System.out.println(carreraRepository.findById(carrera.getId()));
        //InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);
    System.out.println(inscripcionCarreraRepository.findById(inscCarrera.getId()));
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
        carrera.setInscripciones(insCarreras);
        carrera = carreraRepository.save(carrera);
    System.out.println(carreraRepository.findById(carrera.getId()));
        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);
    System.out.println(asignaturaRepository.findById(asignatura.getId()));
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);
    System.out.println(carreraRepository.findById(carrera.getId()));
        //Crear Docente
        Docente docente = new Docente();
        docente.setDocumento("12345678");
        docente.setNombre("John");
        docente.setApellido("Doe");
        docente = docenteRepository.save(docente);
    System.out.println(docenteRepository.findById(docente.getId()));
        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2024, 8, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(90);
        curso.setDocente(docente);
        curso = cursoRepository.save(curso);
        List<Curso> cursoLista = new ArrayList<>();
        cursoLista.add(curso);
        asignatura.setCursos(cursoLista);
        asignatura = asignaturaRepository.save(asignatura);

        //Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso = new InscripcionCurso();
        inscripcionCurso.setFechaInscripcion(LocalDate.now());
        inscripcionCurso.setEstudiante(estudiante);
        inscripcionCurso.setCurso(curso);
        inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
        inscripcionCurso = inscripcionCursoRepository.save(inscripcionCurso);

        List<InscripcionCurso> inscripcionCursoLista = new ArrayList<>();
        inscripcionCursoLista.add(inscripcionCurso);
        curso.setInscripciones(inscripcionCursoLista);
        curso = cursoRepository.save(curso);
    System.out.println(cursoRepository.findById(curso.getId()));
        //Crear Horario
        Horario horario = new Horario();
        horario.setDia(DiaSemana.LUNES);
        horario.setHoraInicio(LocalTime.of(9, 0));
        horario.setHoraFin(LocalTime.of(11,0));
        horario.setCurso(curso);
        horario = horarioRepository.save(horario);
    System.out.println(horarioRepository.findById(horario.getId()));
        //Crear Horario
        Horario horario2 = new Horario();
        horario2.setDia(DiaSemana.MIERCOLES);
        horario2.setHoraInicio(LocalTime.of(10, 0));
        horario2.setHoraFin(LocalTime.of(12,0));
        horario2.setCurso(curso);
        horario2 = horarioRepository.save(horario2);
    System.out.println(horarioRepository.findById(horario2.getId()));
        List<Horario> horarios = new ArrayList<>();
        horarios.add(horario);
        horarios.add(horario2);
        curso.setHorarios(horarios);
        curso = cursoRepository.save(curso);
        mockMvc.perform(get("/inscripcionCurso/cursos-inscripto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].cursoId").value(curso.getId()))
                .andExpect(jsonPath("$[0].asignaturaNombre").value(asignatura.getNombre()))
                .andExpect(jsonPath("$[0].docenteNombre").value(docente.getNombre()))
                .andExpect(jsonPath("$[0].docenteApellido").value(docente.getApellido()));
    }
}


