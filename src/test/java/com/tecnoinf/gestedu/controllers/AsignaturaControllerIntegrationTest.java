package com.tecnoinf.gestedu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.services.interfaces.AsignaturaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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
    private AsignaturaService asignaturaService;

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

}