package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionExamen;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionExamenRepository {
        List<InscripcionExamen> findAllByEstudianteIdAndExamenAsignaturaId(Long estudianteId, Long asignaturaId);
}
