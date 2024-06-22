package com.tecnoinf.gestedu.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tecnoinf.gestedu.dtos.curso.ActaCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;

public interface CursoService {
    CursoDTO createCurso(CursoDTO nuevoCurso);
    HorarioDTO addHorarioToCurso(Long cursoId, HorarioDTO nuevoHorario);
    List<UsuarioDTO> getEstudiantesByCurso(Long cursoId);
    CursoDTO getCursoPorId(Long cursoId);
    Page<HorarioDTO> getHorariosByCurso(Long cursoId, Pageable pageable);
    List<InscripcionCursoCalificacionDTO> obtenerCalificaciones(Long id);
    ActaCursoDTO generarActaCurso(Long id);
}
