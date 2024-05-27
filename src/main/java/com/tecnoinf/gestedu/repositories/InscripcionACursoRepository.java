package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.InscripcionACurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionACursoRepository extends JpaRepository<InscripcionACurso, Long> {
    boolean existsByEstudianteAndCurso(Estudiante estudiante, Curso curso);
    List<InscripcionACurso> findByEstudianteId(Long estudianteId);

}
