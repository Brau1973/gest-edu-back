package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;

public interface AsignaturaService {
    AsignaturaDTO createAsignatura(CreateAsignaturaDTO createAsignaturaDto);
}
