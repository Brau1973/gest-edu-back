package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionCursoRepository extends JpaRepository<InscripcionCurso, Long> {
    List<InscripcionCurso> findAllByEstudianteIdAndCursoAsignaturaId(Long estudianteId, Long asignaturaId);
}
