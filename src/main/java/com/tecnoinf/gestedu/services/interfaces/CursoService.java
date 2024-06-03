package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;

import java.util.List;

public interface CursoService {
    CursoDTO createCurso(CursoDTO nuevoCurso);
    HorarioDTO addHorarioToCurso(Long cursoId, HorarioDTO nuevoHorario);
    List<UsuarioDTO> getEstudiantesByCurso(Long cursoId);
    CursoDTO getCursoPorId(Long cursoId);
}
