package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.PeriodoExamen;
import com.tecnoinf.gestedu.repositories.PeriodoExamenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PeriodoExamenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PeriodoExamenRepository periodoExamenRepository;

    private PeriodoExamen periodoExamen;
    private Carrera carrera;

    @BeforeEach
    public void setUp() {
        carrera = new Carrera();
        entityManager.persist(carrera);
        periodoExamen = new PeriodoExamen();
        periodoExamen.setCarrera(carrera);
        periodoExamen.setFechaInicio(LocalDate.now());
        periodoExamen.setFechaFin(LocalDate.now().plusDays(1));
        entityManager.persist(periodoExamen);
    }

    @Test
    public void whenFindAllByCarreraId_thenReturnPageOfPeriodoExamen() {
        Page<PeriodoExamen> found = periodoExamenRepository.findAllByCarreraId(periodoExamen.getCarrera().getId(), PageRequest.of(0, 10));
        assertThat(found.getContent()).contains(periodoExamen);
    }

    @Test
    public void whenFindByFechaInicioAndFechaFinAndCarreraId_thenReturnOptionalOfPeriodoExamen() {
        Optional<PeriodoExamen> found = periodoExamenRepository.findByFechaInicioAndFechaFinAndCarreraId(periodoExamen.getFechaInicio(), periodoExamen.getFechaFin(), periodoExamen.getCarrera().getId());
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(periodoExamen);
    }

    @Test
    public void whenFindAllByCarreraId_thenReturnListOfPeriodoExamen() {
        List<PeriodoExamen> found = periodoExamenRepository.findAllByCarreraId(periodoExamen.getCarrera().getId());
        assertThat(found).contains(periodoExamen);
    }
}