package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.curso.CursoHorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;

import java.security.Principal;
import java.util.List;

public interface InscripcionCursoService {
    InscripcionCursoDTO createInscripcionCurso(InscripcionCursoDTO inscripcionCursoDTO);
    List<InscripcionCursoCalificacionDTO> registrarCalificaciones(Long id, List<InscripcionCursoCalificacionDTO> calificaciones);
    void deleteInscripcionCurso(Long inscripcionCursoId);
    List<CursoHorarioDTO> listarCursosHorariosInscriptos(Long idEstudiante);
    InscripcionCursoDTO darseDeBajaCurso(Long inscripcionCursoId, String name);
}
