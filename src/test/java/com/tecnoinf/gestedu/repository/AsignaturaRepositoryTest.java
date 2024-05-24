package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AsignaturaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    private Carrera carrera1;
    private Carrera carrera2;

    @BeforeEach
    public void setUp() {
        carrera1 = new Carrera();
        carrera1.setNombre("Carrera 1");
        entityManager.persist(carrera1);

        carrera2 = new Carrera();
        carrera2.setNombre("Carrera 2");
        entityManager.persist(carrera2);

        for (int i = 1; i <= 6; i++) {
            Asignatura asignatura = new Asignatura();
            asignatura.setNombre("Asignatura " + i);
            asignatura.setCarrera(i <= 4 ? carrera1 : carrera2);
            entityManager.persist(asignatura);
        }

        entityManager.flush();
    }

    @Test
    public void findAllByCarreraId_returnsAsignaturas() {
        var result = asignaturaRepository.findAllByCarreraId(carrera1.getId(), PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getContent()).extracting(Asignatura::getNombre).containsExactlyInAnyOrder("Asignatura 1", "Asignatura 2", "Asignatura 3", "Asignatura 4");
    }
}