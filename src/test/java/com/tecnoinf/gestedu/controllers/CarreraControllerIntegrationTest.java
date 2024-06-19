package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.tecnoinf.gestedu.config.TestConfig;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.*;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CarreraControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    InscripcionCursoRepository inscripcionCursoRepository;

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

    @Mock
    private FirebaseApp firebaseApp;

    @MockBean
    private ActividadService actividadService;

    @BeforeEach
    public void setup() {
        // Configurar el mock para que el método registrarActividad no haga nada
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "FUNCIONARIO" })
    @Transactional
    public void getCursosHorariosCarreras() throws Exception {
        // Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("estudiante@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        // Crear Carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // InscripcionCarrera
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

        // Crear Asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);

        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        carrera.setAsignaturas(asignaturas);
        carrera = carreraRepository.save(carrera);

        // Crear Docente
        Docente docente = new Docente();
        docente.setDocumento("12345678");
        docente.setNombre("John");
        docente.setApellido("Doe");
        docente = docenteRepository.save(docente);

        // Crear Curso
        Curso curso = new Curso();
        curso.setFechaInicio(LocalDate.of(2024, 8, 15));
        curso.setFechaFin(LocalDate.of(2025, 7, 15));
        curso.setAsignatura(asignatura);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(90);
        curso.setDocente(docente);
        curso = cursoRepository.save(curso);

        List<Curso> cursoLista = new ArrayList<>();
        cursoLista.add(curso);
        asignatura.setCursos(cursoLista);
        asignatura = asignaturaRepository.save(asignatura);

        // Crear InscripcionCurso
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

        // Crear Horario
        Horario horario = new Horario();
        horario.setDia(DiaSemana.LUNES);
        horario.setHoraInicio(LocalTime.of(9, 0));
        horario.setHoraFin(LocalTime.of(11, 0));
        horario.setCurso(curso);
        horario = horarioRepository.save(horario);

        // Crear segundo Horario
        Horario horario2 = new Horario();
        horario2.setDia(DiaSemana.MIERCOLES);
        horario2.setHoraInicio(LocalTime.of(10, 0));
        horario2.setHoraFin(LocalTime.of(12, 0));
        horario2.setCurso(curso);
        horario2 = horarioRepository.save(horario2);

        List<Horario> horarios = new ArrayList<>();
        horarios.add(horario);
        horarios.add(horario2);
        curso.setHorarios(horarios);
        curso = cursoRepository.save(curso);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Perform the GET request and verify the response
        mockMvc.perform(get("/carreras/" + carrera.getId() + "/horarios-cursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].fechaInicio").value(curso.getFechaInicio().toString()))
                .andExpect(jsonPath("$.content[0].fechaFin").value(curso.getFechaFin().toString()))
                .andExpect(jsonPath("$.content[0].diasPrevInsc").value(curso.getDiasPrevInsc()))
                .andExpect(jsonPath("$.content[0].estado").value(curso.getEstado().toString()))
                .andExpect(jsonPath("$.content[0].asignaturaNombre").value(asignatura.getNombre()))
                .andExpect(jsonPath("$.content[0].docenteNombre").value(docente.getNombre()))
                .andExpect(jsonPath("$.content[0].docenteApellido").value(docente.getApellido()))
                .andExpect(jsonPath("$.content[0].horarios").isArray())
                .andExpect(jsonPath("$.content[0].horarios[0].dia").value(horario.getDia().toString()))
                .andExpect(jsonPath("$.content[0].horarios[0].horaInicio").value(horario.getHoraInicio().format(timeFormatter)))
                .andExpect(jsonPath("$.content[0].horarios[0].horaFin").value(horario.getHoraFin().format(timeFormatter)))
                .andExpect(jsonPath("$.content[0].horarios[1].dia").value(horario2.getDia().toString()))
                .andExpect(jsonPath("$.content[0].horarios[1].horaInicio").value(horario2.getHoraInicio().format(timeFormatter)))
                .andExpect(jsonPath("$.content[0].horarios[1].horaFin").value(horario2.getHoraFin().format(timeFormatter)));
        /*Respuesta:
        "cursoId":4,"fechaInicio":"2024-08-15","fechaFin":"2025-07-15","diasPrevInsc":90,"estado":"ACTIVO",
        "asignaturaNombre":"Asignatura Test 1","docenteNombre":"John","docenteApellido":"Doe",
        "horarios":[{"dia":"LUNES","horaInicio":"09:00:00","horaFin":"11:00:00"},
        {"dia":"MIERCOLES","horaInicio":"10:00:00","horaFin":"12:00:00"}]}]
         */
    }
}
