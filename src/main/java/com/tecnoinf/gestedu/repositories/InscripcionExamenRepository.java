package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionCarrera;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionExamenRepository extends JpaRepository<InscripcionExamen, Long> {
    List<InscripcionExamen> findAllByEstudianteIdAndExamenAsignaturaId(Long estudianteId, Long asignaturaId);
}
