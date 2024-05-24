package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.models.Carrera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstudianteService {
    Page<Carrera> getCarrerasNoInscripto(String email, Pageable pageable);
}