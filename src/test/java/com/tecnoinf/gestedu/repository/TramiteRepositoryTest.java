package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Tramite;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.TramiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
public class TramiteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TramiteRepository tramiteRepository;

    private Estudiante estudiante;
    private Carrera carrera;

    @BeforeEach
    public void setUp() {
        estudiante = new Estudiante();
        estudiante.setNombre("Test Name");
        estudiante.setPassword("Test Password");
        estudiante.setCi("Test CI");
        estudiante.setEmail("test@example.com");
        estudiante.setApellido("Test Surname");
        entityManager.persist(estudiante);

        carrera = new Carrera();
        entityManager.persist(carrera);
    }

    @Transactional
    @Test
    public void existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado_returnsTrue_whenTramiteExists() {
        Tramite tramite = new Tramite();
        tramite.setUsuarioSolicitante(estudiante);
        tramite.setCarrera(carrera);
        tramite.setTipo(TipoTramite.INSCRIPCION_A_CARRERA);
        tramite.setEstado(EstadoTramite.PENDIENTE);
        entityManager.persist(tramite);

        boolean exists = tramiteRepository.existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(estudiante, carrera, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE);

        assertThat(exists).isTrue();
    }

    @Test
    public void existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado_returnsFalse_whenTramiteDoesNotExist() {
        boolean exists = tramiteRepository.existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(estudiante, carrera, TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE);

        assertThat(exists).isFalse();
    }
}