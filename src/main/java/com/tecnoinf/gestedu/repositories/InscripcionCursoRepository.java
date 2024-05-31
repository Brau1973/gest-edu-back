package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionCursoRepository extends JpaRepository<InscripcionCurso, Long> {
    List<InscripcionCurso> findByEstudianteIdAndCursoAsignaturaId(Long estudianteId, Long asignaturaId);
    List<InscripcionCurso> findByCalificacionAndEstudianteId(CalificacionCurso calificacionCurso, Long estudianteId);
}
