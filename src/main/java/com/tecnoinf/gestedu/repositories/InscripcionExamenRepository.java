package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionExamenRepository extends JpaRepository<InscripcionExamen, Long> {
    Optional<InscripcionExamen> findByEstudianteIdAndExamenId(Long estudianteId, Long examenId);
    List<InscripcionExamen> findAllByEstudianteIdAndExamenAsignaturaId(Long estudianteId, Long asignaturaId);
    List<InscripcionExamen>findByCalificacionAndEstudianteId(CalificacionExamen calificacionExamen, Long id);
}
