package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.services.interfaces.AsignaturaService;
import com.tecnoinf.gestedu.services.interfaces.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

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
    private CursoService cursoService;

    @Test
    @Transactional
    public void testRegisterCurso_Success() throws Exception {
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test");
        asignatura = asignaturaRepository.save(asignatura);
        Docente docente = new Docente();
        docente.setNombre("John");
        docente.setApellido("Doe");
        docente.setDocumento("12223334");
        docente = docenteRepository.save(docente);
        CursoDTO cursoDTO = new CursoDTO();

        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        cursoDTO.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        cursoDTO.setFechaFin(fechaFin);

        //cursoDTO.setSemestre(2);
        cursoDTO.setAsignaturaId(asignatura.getId());
        cursoDTO.setDocenteId(docente.getId());

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testRegisterHorario_Curso_Success() throws Exception {
        Curso curso = new Curso();
        curso = cursoRepository.getReferenceById(1L);

        HorarioDTO horarioDTO = new HorarioDTO();
        horarioDTO.setDia(DiaSemana.LUNES);
        horarioDTO.setHoraInicio(LocalTime.of(9, 0));
        horarioDTO.setHoraFin(LocalTime.of(11, 0));
        horarioDTO.setCursoId(curso.getId());

        mockMvc.perform(post("/cursos/horarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horarioDTO)))
                .andExpect(status().isOk());
    }


}