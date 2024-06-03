package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;

public interface InscripcionCarreraService {
    InscripcionCarreraDTO createInscripcionCarrera(Carrera carrera, Estudiante estudiante);
}
