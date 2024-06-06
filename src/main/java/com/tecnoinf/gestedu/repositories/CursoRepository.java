package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByDocenteId(Long id);
}
