package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    //Page<Curso> findAllByAsignaturaId(Long id, Pageable pageable);
}
