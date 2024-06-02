package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.models.InscripcionCarrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InscripcionCarreraRepository extends JpaRepository<InscripcionCarrera, Long> {
    @Query("SELECT ic FROM InscripcionCarrera ic JOIN ic.estudiante e WHERE ic.estudiante.id = :estudianteId")
    List<InscripcionCarrera> findInscripcionCarreraEstudianteById(Long estudianteId);
    InscripcionCarrera findByEstudianteIdAndCarreraId(Long id, Long carreraId);
}
