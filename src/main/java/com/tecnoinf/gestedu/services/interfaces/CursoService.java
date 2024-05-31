package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;

public interface CursoService {
    CursoDTO createCurso(CursoDTO nuevoCurso);
    HorarioDTO addHorarioToCurso(Long cursoId, HorarioDTO nuevoHorario);
}