package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionCursoRepository extends JpaRepository<InscripcionCurso, Long> {
    List<InscripcionCurso> findByEstudianteIdAndCursoAsignaturaId(Long estudianteId, Long asignaturaId);
    List<InscripcionCurso> findByCalificacionAndEstudianteId(CalificacionCurso calificacionCurso, Long estudianteId);
    @Query("SELECT ic FROM InscripcionCurso ic JOIN ic.estudiante e WHERE ic.estudiante.id = :estudianteId")
    List<InscripcionCurso> findInscripcionCursoEstudianteById(Long estudianteId);
    @Query("SELECT ic FROM InscripcionCurso ic JOIN ic.estudiante e JOIN ic.curso c WHERE ic.estudiante.id = :estudianteId AND c.id = :cursoId")
    Optional<InscripcionCurso> findInscripcionCursoEstudianteByEstudianteIdAndCursoId(Long estudianteId, Long cursoId);
}