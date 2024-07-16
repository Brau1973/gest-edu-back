package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByDocenteId(Long id);
    List<Curso> findByAsignaturaIdAndEstado(Long asignaturaId, Estado estado);
    List<Curso> findByAsignaturaCarreraId(Long carreraId);
}
