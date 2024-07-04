package com.tecnoinf.gestedu.repository;

import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.HorarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class HorarioRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HorarioRepository horarioRepository;

    @Test
    public void whenfindHorariosBySemestreAndDia_thenReturnListHorario(){
        // Crear carrera
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera Test");
        entityManager.persist(carrera);
        entityManager.flush();

        // Crear asignatura
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Asignatura Test 1");
        asignatura.setCarrera(carrera);
        asignatura.setSemestrePlanEstudio(1);
        entityManager.persist(asignatura);
        entityManager.flush();

        //Crear Curso
        Curso curso = new Curso();
        LocalDate fechaInicio = LocalDate.of(2025, 3, 15); // Año, Mes (1-12), Día
        curso.setFechaInicio(fechaInicio);
        LocalDate fechaFin = LocalDate.of(2025, 07, 15); // Año, Mes (1-12), Día
        curso.setFechaFin(fechaFin);
        curso.setAsignatura(asignatura);
        //curso.setDocente(docente);
        curso.setEstado(Estado.ACTIVO);
        curso.setDiasPrevInsc(30);
        entityManager.persist(curso);
        entityManager.flush();

        //Crear Horario
        Horario horario = new Horario();
        horario.setDia(DiaSemana.LUNES);
        horario.setHoraInicio(LocalTime.of(9, 0 ));
        horario.setHoraFin(LocalTime.of(11, 0));
        horario.setCurso(curso);
        entityManager.persist(horario);
        entityManager.flush();

        List<Horario> foundHorarios = horarioRepository.findHorariosBySemestreDiaAndCarrera(curso.getAsignatura().getSemestrePlanEstudio(), horario.getDia(), curso.getAsignatura().getCarrera().getId());

        assertThat(foundHorarios).isNotNull();
        assertThat(foundHorarios).isNotEmpty();

        List<Horario> foundHorarios2 = horarioRepository.findHorariosBySemestreDiaAndCarrera(1, DiaSemana.MARTES, curso.getAsignatura().getCarrera().getId());

        assertThat(foundHorarios2).isEmpty();

    }
}
