package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
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

    @Test
    @Transactional
    public void testCreateDuplicateAsignatura_Fails() throws Exception {
        // Crear una carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        carrera = carreraRepository.save(carrera);

        // Crear una asignatura
        Asignatura asignatura  = new Asignatura();
        asignatura.setNombre("Asignatura Test");
        asignatura.setCarrera(carrera);
        asignaturaRepository.save(asignatura);

        // Intentar crear otra asignatura con el mismo nombre en la misma carrera
        CreateAsignaturaDTO createDuplicateAsignaturaDto = new CreateAsignaturaDTO();
        createDuplicateAsignaturaDto.setNombre("Asignatura Test");
        createDuplicateAsignaturaDto.setCarreraId(carrera.getId());

        mockMvc.perform(post("/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDuplicateAsignaturaDto)))
                .andExpect(status().isBadRequest());  // Asume que el servidor devuelve un estado 400 (Bad Request) cuando se intenta crear una asignatura con un nombre duplicado
    }
}
