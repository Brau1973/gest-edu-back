package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Carrera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CarreraRepository extends JpaRepository<Carrera, Long>, JpaSpecificationExecutor<Carrera> {
    boolean existsByNombre(String nombre);
    Page<Carrera> findByExistePlanEstudioFalse(Pageable pageable);
}
