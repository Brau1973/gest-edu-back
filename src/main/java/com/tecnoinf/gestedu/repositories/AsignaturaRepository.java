package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Asignatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long>{
    Page<Asignatura> findAllByCarreraId(Long id, Pageable pageable);
    boolean existsByNombreAndCarreraId(String nombre, Long carreraId);
    boolean existsByNombre(String comunicacionOralYEscrita);
    List<Asignatura> findByCarreraId(Long id);
    Asignatura findByNombreAndCarreraId(String nombre, Long carreraId);

    // Método para obtener las asignaturas previas de una asignatura específica
    @Query("SELECT a.previas FROM Asignatura a WHERE a.id = :asignaturaId")
    List<Asignatura> findPreviasByAsignaturaId(Long asignaturaId);
}
