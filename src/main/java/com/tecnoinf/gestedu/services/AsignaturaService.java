package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;

import java.util.List;

public interface AsignaturaService {
    AsignaturaDTO createAsignatura(CreateAsignaturaDTO createAsignaturaDto);
    List<AsignaturaDTO> getPrevias(Long asignaturaId);
    AsignaturaDTO addPrevia(Long asignaturaId, Long previaId);
    List<AsignaturaDTO> getNoPrevias(Long asignaturaId);
}
