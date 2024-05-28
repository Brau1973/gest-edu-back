package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionCursoRepository {
    List<InscripcionCurso> findAllByEstudianteIdAndCursoAsignaturaId(Long estudianteId, Long asignaturaId);
}
