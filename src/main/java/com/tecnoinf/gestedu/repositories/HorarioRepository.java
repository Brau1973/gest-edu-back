package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Horario;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    @Query("SELECT h FROM Horario h JOIN h.curso c WHERE c.asignatura.semestrePlanEstudio = :semestre AND h.dia = :dia")
    List<Horario> findHorariosBySemestreAndDia(Integer semestre, DiaSemana dia);

    @Query("SELECT h FROM Horario h WHERE h.curso.id = :cursoId")
    List<Horario> findHorariosByCursoId(Long cursoId);

    @Query("SELECT h FROM Horario h " +
            "JOIN h.curso c " +
            "JOIN c.asignatura a " +
            "JOIN a.carrera car " +
            "WHERE a.semestrePlanEstudio = :semestre " +
            "AND h.dia = :dia " +
            "AND car.id = :carreraId")
    List<Horario> findHorariosBySemestreDiaAndCarrera(Integer semestre,DiaSemana dia, Long carreraId);
}
