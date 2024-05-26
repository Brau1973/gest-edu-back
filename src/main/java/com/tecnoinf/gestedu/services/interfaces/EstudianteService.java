package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EstudianteService {
    Page<BasicInfoCarreraDTO> getCarrerasNoInscripto(String email, Pageable pageable);
    Page<BasicInfoUsuarioDTO> obtenerEstudiantes(Pageable pageable);
    Optional<BasicInfoUsuarioDTO> obtenerEstudiantePorCi(String ci);
}