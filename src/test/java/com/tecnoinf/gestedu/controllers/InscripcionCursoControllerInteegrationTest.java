package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=update"
})
@AutoConfigureMockMvc
public class InscripcionCursoControllerInteegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionCarreraRepository inscripcionCarreraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
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
        inscCarrera.setFechaInscripcion(LocalDateTime.of(2025, 7, 15,0,0,0));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value(estudiante.getId()))
                .andExpect(jsonPath("$.cursoId").value(curso.getId()));
    }

}
