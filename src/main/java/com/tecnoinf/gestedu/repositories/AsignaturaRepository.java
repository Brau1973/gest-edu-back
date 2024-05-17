package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Asignatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long>{
    Page<Asignatura> findAllByCarreraId(Long id, Pageable pageable);
    boolean existsByNombreAndCarreraId(String nombre, Long carreraId);
    boolean existsByNombre(String comunicacionOralYEscrita);
}
