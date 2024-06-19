package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.tecnoinf.gestedu.config.TestConfig;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.AsignaturaService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Import(TestConfig.class)
@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=update"
})
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AsignaturaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AsignaturaService asignaturaService;

    @MockBean
    private ActividadService actividadService;

    @InjectMocks
    private InscripcionCursoController inscripcionCursoController;

    @Mock
    private FirebaseApp firebaseApp;

    @BeforeEach
    public void setup() {
        // Configurar el mock para que el método registrarActividad no haga nada
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    @Test
    @Transactional
    public void testCreateAsignatura_Success() throws Exception {
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        CreateAsignaturaDTO createAsignaturaDto = new CreateAsignaturaDTO();
        createAsignaturaDto.setNombre("Asignatura Test");
        createAsignaturaDto.setCarreraId(carrera.getId());

        mockMvc.perform(post("/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAsignaturaDto)))
                .andExpect(status().isOk());
    }

//    @Test
//    @Transactional
//    public void testCreateDuplicateAsignatura_Fails() throws Exception {
//        // Crear una carrera
//        Carrera carrera = new Carrera();
//        carrera.setNombre("Carrera Test");
//        carrera = carreraRepository.save(carrera);
//
//        // Crear una asignatura
//        Asignatura asignatura  = new Asignatura();
//        asignatura.setNombre("Asignatura Test");
//        asignatura.setCarrera(carrera);
//        asignaturaRepository.save(asignatura);
//
//        // Intentar crear otra asignatura con el mismo nombre en la misma carrera
//        CreateAsignaturaDTO createDuplicateAsignaturaDto = new CreateAsignaturaDTO();
//        createDuplicateAsignaturaDto.setNombre("Asignatura Test");
//        createDuplicateAsignaturaDto.setCarreraId(carrera.getId());
//
//        mockMvc.perform(post("/asignaturas")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDuplicateAsignaturaDto)))
//                .andExpect(status().isBadRequest());  // Asume que el servidor devuelve un estado 400 (Bad Request) cuando se intenta crear una asignatura con un nombre duplicado
//    }

    @Test
    @Transactional
    public void testGetPrevias() throws Exception {
        // Crear una carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear asignaturas
        Asignatura asignatura1 = new Asignatura();
        asignatura1.setNombre("Asignatura Test 1");
        asignatura1.setCarrera(carrera);
        asignatura1 = asignaturaRepository.save(asignatura1);

        Asignatura asignatura2 = new Asignatura();
        asignatura2.setNombre("Asignatura Test 2");
        asignatura2.setCarrera(carrera);
        asignatura2.setPrevias(Arrays.asList(asignatura1));
        asignatura2 = asignaturaRepository.save(asignatura2);

        mockMvc.perform(get("/asignaturas/" + asignatura2.getId() + "/previas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(asignatura1.getId().intValue())));
    }

    @Test
    @Transactional
    public void testGetNoPrevias() throws Exception {
        // Crear una carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear asignaturas
        Asignatura asignatura1 = new Asignatura();
        asignatura1.setNombre("Asignatura Test 1");
        asignatura1.setCarrera(carrera);
        asignatura1 = asignaturaRepository.save(asignatura1);

        Asignatura asignatura2 = new Asignatura();
        asignatura2.setNombre("Asignatura Test 2");
        asignatura2.setCarrera(carrera);
        asignatura2 = asignaturaRepository.save(asignatura2);

        mockMvc.perform(get("/asignaturas/" + asignatura1.getId() + "/no-previas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(asignatura2.getId().intValue())));
    }

    @Test
    @Transactional
    public void testAddPrevia() throws Exception {
        // Crear una carrera
        Carrera carrera = new Carrera();
        carrera.setExistePlanEstudio(true);
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear asignaturas
        Asignatura asignatura1 = new Asignatura();
        asignatura1.setNombre("Asignatura Test 1");
        asignatura1.setCarrera(carrera);
        asignatura1.setSemestrePlanEstudio(1);
        asignatura1 = asignaturaRepository.save(asignatura1);

        Asignatura asignatura2 = new Asignatura();
        asignatura2.setNombre("Asignatura Test 2");
        asignatura2.setCarrera(carrera);
        asignatura2.setSemestrePlanEstudio(2);
        asignatura2 = asignaturaRepository.save(asignatura2);

        mockMvc.perform(post("/asignaturas/" + asignatura2.getId() + "/previa/" + asignatura1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getCursosDeAsignatura() throws Exception{
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

        //Crear Curso
        Curso curso2 = new Curso();
        fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso2.setFechaInicio(fechaInicio);
        fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        curso2.setFechaFin(fechaFin);
        curso2.setAsignatura(asignatura);
        curso2.setDocente(docente);
        curso2.setEstado(Estado.ACTIVO);
        curso2.setDiasPrevInsc(30);
        curso2 = cursoRepository.save(curso2);

        //Crear Curso
        Curso curso3 = new Curso();
        fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso3.setFechaInicio(fechaInicio);
        fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        curso3.setFechaFin(fechaFin);
        curso3.setAsignatura(asignatura);
        curso3.setDocente(docente);
        curso3.setEstado(Estado.ACTIVO);
        curso3.setDiasPrevInsc(30);
        curso3 = cursoRepository.save(curso3);

        List<Curso> cursos = new ArrayList<>();
        cursos.add(curso);
        cursos.add(curso2);
        cursos.add(curso3);
        asignatura.setCursos(cursos);
        asignatura = asignaturaRepository.save(asignatura);

        mockMvc.perform(get("/asignaturas/" + asignatura.getId() + "/cursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].fechaInicio").value(curso.getFechaInicio().toString()))
                .andExpect(jsonPath("$[0].fechaFin").value(curso.getFechaFin().toString()))
                .andExpect(jsonPath("$[0].diasPrevInsc", is(curso.getDiasPrevInsc())))
                .andExpect(jsonPath("$[0].estado").value(curso.getEstado().toString()));
    }
}