package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {
    boolean existsByFechaAndAsignatura(LocalDateTime fecha, Asignatura asignatura);
}
