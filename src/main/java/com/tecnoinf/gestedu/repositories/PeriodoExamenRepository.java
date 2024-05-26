package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.PeriodoExamen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PeriodoExamenRepository extends JpaRepository<PeriodoExamen, Long> {
    Page<PeriodoExamen> findAllByCarreraId(Long id, Pageable pageable);
    Optional<PeriodoExamen> findByFechaInicioAndFechaFinAndCarreraId(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long carreraId);
}
