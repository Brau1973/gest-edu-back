package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.InscripcionCarreraRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CarreraRepositoryTest {

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private InscripcionCarreraRepository inscripcionCarreraRepository;

    @Test
    public void findCarrerasWithPlanEstudioAndEstudianteNotInscriptoReturnsExpectedData() {
        // Crear tres carreras
        Carrera carrera1 = new Carrera();
        carrera1.setExistePlanEstudio(true);
        Carrera carrera2 = new Carrera();
        carrera2.setExistePlanEstudio(true);
        Carrera carrera3 = new Carrera();
        carrera3.setExistePlanEstudio(false);

        // Guardar las carreras en la base de datos
        carrera1 = carreraRepository.save(carrera1);
        carrera2 = carreraRepository.save(carrera2);
        carreraRepository.save(carrera3);

        // Crear un estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setApellido("Apellido");
        estudiante.setEmail("test@test.com");
        estudiante.setNombre("Nombre");
        estudiante.setPassword("Password");
        estudiante.setCi("CI");
        estudiante = estudianteRepository.save(estudiante);

        // Inscribir al estudiante en una de las carreras con plan de estudio = true
        InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
        inscripcionCarrera.setEstudiante(estudiante);
        inscripcionCarrera.setCarrera(carrera1);
        inscripcionCarreraRepository.save(inscripcionCarrera);

        // Llamar al método que se está probando
        Page<Carrera> result = carreraRepository.findCarrerasWithPlanEstudioAndEstudianteNotInscripto(estudiante.getId(), PageRequest.of(0, 10));

        // Verificar que el resultado es el esperado
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(carrera2.getId(), result.getContent().get(0).getId());
    }
}