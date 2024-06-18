package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByUsuarioId(Long id);
}
