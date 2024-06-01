package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionCarrera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionCarreraRepository extends JpaRepository<InscripcionCarrera, Long> {
    InscripcionCarrera findByEstudianteIdAndCarreraId(Long id, Long carreraId);
}
