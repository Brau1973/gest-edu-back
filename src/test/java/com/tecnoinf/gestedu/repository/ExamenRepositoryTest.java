package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Examen;
import com.tecnoinf.gestedu.models.enums.Estado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.tecnoinf.gestedu.repositories.ExamenRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ExamenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExamenRepository examenRepository;

    @Test
    public void whenFindById_thenReturnExamen() {
        Examen givenExamen = new Examen();
        givenExamen.setFecha(LocalDateTime.now());
        givenExamen.setDiasPrevInsc(5);
        givenExamen.setEstado(Estado.ACTIVO);
        entityManager.persist(givenExamen);
        entityManager.flush();

        Examen foundExamen = examenRepository.findById(givenExamen.getId()).orElse(null);

        assertThat(foundExamen).isNotNull();
        assertThat(foundExamen.getFecha()).isEqualTo(givenExamen.getFecha());
        assertThat(foundExamen.getDiasPrevInsc()).isEqualTo(givenExamen.getDiasPrevInsc());
        assertThat(foundExamen.getEstado()).isEqualTo(givenExamen.getEstado());
    }

    @Test
    public void whenExistsByFechaAndAsignatura_thenReturnTrue() {
        Carrera carrera = new Carrera();
        entityManager.persist(carrera);
        Asignatura asignatura = new Asignatura();
        asignatura.setCarrera(carrera);
        entityManager.persist(asignatura);

        LocalDateTime examenFecha = LocalDateTime.now().withSecond(0).withNano(0);

        Examen examen = new Examen();
        examen.setFecha(examenFecha);
        examen.setAsignatura(asignatura);
        entityManager.persist(examen);
        entityManager.flush();

        boolean exists = examenRepository.existsByFechaAndAsignatura(examenFecha, asignatura);
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByAsignaturaId_thenReturnExamenList() {
        Carrera carrera = new Carrera();
        entityManager.persist(carrera);
        Asignatura asignatura = new Asignatura();
        asignatura.setCarrera(carrera);
        entityManager.persist(asignatura);
        Examen examen1 = new Examen();
        examen1.setAsignatura(asignatura);
        entityManager.persist(examen1);
        Examen examen2 = new Examen();
        examen2.setAsignatura(asignatura);
        entityManager.persist(examen2);
        entityManager.flush();

        List<Examen> examenes = examenRepository.findByAsignaturaId(asignatura.getId());
        assertThat(examenes).hasSize(2).containsExactlyInAnyOrder(examen1, examen2);
    }
}