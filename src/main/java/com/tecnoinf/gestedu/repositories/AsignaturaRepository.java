package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long>{

    Page<Asignatura> findAllByCarreraId(Long id, Pageable pageable);
    boolean existsByNombreAndCarreraId(String nombre, Long carreraId);
    boolean existsByNombre(String comunicacionOralYEscrita);
    List<Asignatura> findByCarreraId(Long id);
}