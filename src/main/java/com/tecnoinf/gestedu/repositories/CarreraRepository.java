package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Carrera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarreraRepository extends JpaRepository<Carrera, Long>, JpaSpecificationExecutor<Carrera> {
    boolean existsByNombre(String nombre);
    Page<Carrera> findByExistePlanEstudioFalse(Pageable pageable);

    @Query("SELECT c FROM Carrera c WHERE c.id NOT IN (SELECT ic.carrera.id FROM InscripcionCarrera ic WHERE ic.estudiante.id = :estudianteId) AND c.existePlanEstudio = true")
    Page<Carrera> findCarrerasWithPlanEstudioAndEstudianteNotInscripto(@Param("estudianteId") Long estudianteId, Pageable pageable);

    @Query("SELECT c FROM Carrera c WHERE c.id IN (SELECT ic.carrera.id FROM InscripcionCarrera ic WHERE ic.estudiante.id = :estudianteId) AND c.existePlanEstudio = true")
    Page<Carrera> findCarrerasWithPlanEstudioAndEstudianteInscripto(@Param("estudianteId") Long estudianteId, Pageable pageable);
}
