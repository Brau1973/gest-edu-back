package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Horario;
import com.tecnoinf.gestedu.models.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    @Query("SELECT h FROM Horario h JOIN h.curso c WHERE c.asignatura.semestrePlanEstudio = :semestre AND h.dia = :dia")
    List<Horario> findHorariosBySemestreAndDia(Integer semestre, DiaSemana dia);
}
