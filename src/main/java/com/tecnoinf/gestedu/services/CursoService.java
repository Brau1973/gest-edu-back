package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;

public interface CursoService {
    CursoDTO createCurso(CursoDTO cursoDTO, HorarioDTO horarioDTO, Long docenteId);
}
