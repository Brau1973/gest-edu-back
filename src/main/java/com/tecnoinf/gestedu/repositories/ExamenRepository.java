package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Examen;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import com.tecnoinf.gestedu.models.enums.Estado;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {
    boolean existsByFechaAndAsignatura(LocalDateTime fecha, Asignatura asignatura);
    List<Examen> findByAsignaturaId(Long asignaturaId);
    Page<Examen> findAllByFechaBeforeAndEstado(LocalDateTime now, Estado estado, Pageable pageable);
    boolean existsByDocentesId(Long id);
}
