package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.tecnoinf.gestedu.config.TestConfig;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.*;
import com.tecnoinf.gestedu.repositories.*;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=update"
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EstudianteControllerIntegrationTest {
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
    private TramiteRepository tramiteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Mock
    private FirebaseApp firebaseApp;


    @Test
    @Transactional
    @WithMockUser(username = "johndoe@gmail.com", authorities = { "ROL_ESTUDIANTE" })
    public void obtenerAsignaturasParaInscripcion() throws Exception {
        // Crear Estudiante
        Estudiante estudiante1 = new Estudiante();
        estudiante1.setCi("12345678");
        estudiante1.setNombre("John");
        estudiante1.setApellido("Doe");
        estudiante1.setEmail("johndoe@gmail.com");
        estudiante1.setPassword("12345678");
        estudiante1 = usuarioRepository.save(estudiante1);

        // Crear Estudiante
        Estudiante estudiante2 = new Estudiante();
        estudiante2.setCi("12345679");
        estudiante2.setNombre("Jane");
        estudiante2.setApellido("Doe");
        estudiante2.setEmail("janedoe28@gmail.com");
        estudiante2.setPassword("12345678");
        estudiante2 = usuarioRepository.save(estudiante2);


        Estudiante estudiante3 = new Estudiante();
        estudiante3.setCi("12345680");
        estudiante3.setNombre("James");
        estudiante3.setApellido("Doe");
        estudiante3.setEmail("doe28@gmail.com");
        estudiante3.setPassword("12345678");
        estudiante3 = usuarioRepository.save(estudiante3);

        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // InscripcionCarrera
        InscripcionCarrera inscCarrera = new InscripcionCarrera();
        inscCarrera.setCarrera(carrera);
        inscCarrera.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera.setCreditosObtenidos(15);
        inscCarrera.setEstudiante(estudiante1);
        inscCarrera = inscripcionCarreraRepository.save(inscCarrera);

        InscripcionCarrera inscCarrera2 = new InscripcionCarrera();
        inscCarrera2.setCarrera(carrera);
        inscCarrera2.setEstado(EstadoInscripcionCarrera.CURSANDO);
        inscCarrera2.setFechaInscripcion(LocalDate.of(2025, 7, 15));
        inscCarrera2.setCreditosObtenidos(15);
        inscCarrera2.setEstudiante(estudiante2);
        inscCarrera2 = inscripcionCarreraRepository.save(inscCarrera2);
        List<InscripcionCarrera> insCarreras = new ArrayList<>();
        insCarreras.add(inscCarrera);
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

        // Crear Docente
        Docente docente = new Docente();
        docente.setNombre("John");
        docente = docenteRepository.save(docente);

        // Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15);
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 7, 15);
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        curso.setDocente(docente);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        curso = cursoRepository.save(curso);
        List<Curso> cursos = new ArrayList<>();
        cursos.add(curso);
        asignatura.setCursos(cursos);
        asignatura = asignaturaRepository.save(asignatura);

        Curso curso2 = new Curso();
        curso2.setFechaInicio(fechaInicio);
        curso2.setFechaFin(fechaFin);
        curso2.setAsignatura(asignatura);
        curso2.setDocente(docente);
        curso2.setEstado(Estado.ACTIVO);
        curso2.setDiasPrevInsc(3000);
        curso2 = cursoRepository.save(curso2);

        Curso curso3 = new Curso();
        curso3.setFechaInicio(fechaInicio);
        curso3.setFechaFin(fechaFin);
        curso3.setAsignatura(asignatura);
        curso3.setDocente(docente);
        curso3.setEstado(Estado.ACTIVO);
        curso3.setDiasPrevInsc(3000);
        curso3 = cursoRepository.save(curso3);

        List<Curso> auxCursos = new ArrayList<>();
        auxCursos.add(curso);
        auxCursos.add(curso2);
        auxCursos.add(curso3);
        asignatura.setCursos(auxCursos);
        asignatura = asignaturaRepository.save(asignatura);

        // Crear Inscripcion Curso
        InscripcionCurso inscripcionCurso2 = new InscripcionCurso();
        inscripcionCurso2.setFechaInscripcion(LocalDate.now());
        inscripcionCurso2.setEstado(EstadoInscripcionCurso.CURSANDO);
        inscripcionCurso2.setEstudiante(estudiante1);
        inscripcionCurso2.setCurso(curso2);
        inscripcionCurso2.setCalificacion(CalificacionCurso.PENDIENTE);
        inscripcionCurso2 = inscripcionCursoRepository.save(inscripcionCurso2);

        curso2.getInscripciones().add(inscripcionCurso2);
        curso2 = cursoRepository.save(curso2);

        Estudiante finalEstudiante = estudiante1;
        mockMvc.perform(get("/estudiantes/" + carrera.getId() + "/asignaturas-inscripcion")
                        .principal(() -> finalEstudiante.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }
}