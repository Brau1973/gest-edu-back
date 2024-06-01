package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.services.interfaces.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=update"
})
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
    private CursoService cursoService;

    @Test
    @Transactional
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
        horarioDTO.setHoraInicio(LocalTime.of(9, 0 ));
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
}