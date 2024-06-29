package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.tecnoinf.gestedu.config.TestConfig;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.CursoService;
import jakarta.validation.constraints.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import(TestConfig.class)
@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=update"
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private InscripcionCursoRepository inscripcionCursoRepository;

    @Autowired
    private InscripcionCarreraRepository inscripcionCarreraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @MockBean
    private ActividadService actividadService;

    @Mock
    private FirebaseApp firebaseApp;

    @InjectMocks
    private InscripcionCursoController inscripcionCursoController;

    @BeforeEach
    public void setup() {
        // Configurar el mock para que el método registrarActividad no haga nada
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { "ROL_FUNCIONARIO" })
    public void testRegisterCurso_Success() throws Exception {
        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);

        //Crear Docente
        Docente docente = new Docente();
        docente.setNombre("John");
        docente = docenteRepository.save(docente);

        CursoDTO cursoDTO = new CursoDTO();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        cursoDTO.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        cursoDTO.setFechaFin(fechaFin);
        cursoDTO.setAsignaturaId(asignatura.getId());
        cursoDTO.setDocenteId(docente.getId());

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaInicio").value("2025-03-15"))
                .andExpect(jsonPath("$.fechaFin").value("2025-07-15"))
                .andExpect(jsonPath("$.asignaturaId").value(asignatura.getId()))
                .andExpect(jsonPath("$.docenteId").value(docente.getId()));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { "ROL_FUNCIONARIO" })
    public void testRegisterHorario_Curso_Success() throws Exception {
        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);

        //Crear Docente
        Docente docente = new Docente();
        docente.setNombre("John");
        docente = docenteRepository.save(docente);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setDocente(docente);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        curso = cursoRepository.save(curso);
        HorarioDTO horarioDTO = new HorarioDTO();
        horarioDTO.setDia(DiaSemana.LUNES);
        horarioDTO.setHoraInicio(LocalTime.of(9, 0));
        horarioDTO.setHoraFin(LocalTime.of(11, 0));
        //horarioDTO.setCursoId(curso.getId());

        mockMvc.perform(post("/cursos/" + curso.getId() + "/horarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dia").value(DiaSemana.LUNES.name()))
                .andExpect(jsonPath("$.horaInicio").value("09:00:00"))
                .andExpect(jsonPath("$.horaFin").value("11:00:00"));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { "ROL_FUNCIONARIO" })
    public void getCursoById() throws Exception {
        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura = asignaturaRepository.save(asignatura);

        //Crear Docente
        Docente docente = new Docente();
        docente.setNombre("John");
        docente = docenteRepository.save(docente);

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setDocente(docente);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        curso = cursoRepository.save(curso);

        mockMvc.perform(get("/cursos/" + curso.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaInicio").value(curso.getFechaInicio().toString()))
                .andExpect(jsonPath("$.fechaFin").value(curso.getFechaFin().toString()))
                .andExpect(jsonPath("$.diasPrevInsc").value(curso.getDiasPrevInsc()))
                .andExpect(jsonPath("$.estado").value(curso.getEstado().toString()))
                .andExpect(jsonPath("$.asignaturaId").value(curso.getAsignatura().getId()))
                .andExpect(jsonPath("$.docenteId").value(curso.getDocente().getId()));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { "ROL_FUNCIONARIO" })
    public void getEstudiantesByCurso() throws Exception {
        //Crear Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setCi("12345678");
        estudiante.setNombre("John");
        estudiante.setApellido("Doe");
        estudiante.setEmail("johndoe@gmail.com");
        estudiante.setPassword("12345678");
        estudiante = usuarioRepository.save(estudiante);

        //Crear Estudiante
        Estudiante estudiante2 = new Estudiante();
        estudiante2.setCi("12345679");
        estudiante2.setNombre("Jane");
        estudiante2.setApellido("Doe");
        estudiante2.setEmail("janedoe28@gmail.com");
        estudiante2.setPassword("12345678");
        estudiante2 = usuarioRepository.save(estudiante2);

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

        InscripcionCarrera inscCarrera2 = new InscripcionCarrera();
        inscCarrera2.setCarrera(carrera);
        inscCarrera2.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera2.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera2.setCreditosObtenidos(15);
        inscCarrera2.setEstudiante(estudiante2);
        inscCarrera2 = inscripcionCarreraRepository.save(inscCarrera2);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera2);
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

        //Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso2 = new InscripcionCurso();
        inscripcionCurso2.setFechaInscripcion(LocalDate.now());
        inscripcionCurso2.setEstudiante(estudiante2);
        inscripcionCurso2.setCurso(curso);
        inscripcionCurso2.setCalificacion(CalificacionCurso.PENDIENTE);
        inscripcionCurso2 = inscripcionCursoRepository.save(inscripcionCurso2);

        curso.getInscripciones().add(inscripcionCurso2);
        curso = cursoRepository.save(curso);

        mockMvc.perform(get("/cursos/" + curso.getId() + "/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(estudiante.getId()))
                .andExpect(jsonPath("$[0].nombre", is(estudiante.getNombre())))
                .andExpect(jsonPath("$[0].apellido").value(estudiante.getApellido()))
                .andExpect(jsonPath("$[0].ci").value(estudiante.getCi()));
    }
}