package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.curso.InscripcionACursoDTO;
import com.tecnoinf.gestedu.models.InscripcionACurso;

import java.util.List;

public interface CursoService {
    CursoDTO createCurso(HorarioCursoDTO horarioCursoDTO);
    InscripcionACursoDTO inscribirCurso(InscripcionACursoDTO inscripcionCurso);
    boolean isEstudianteInscritoEnCurso(Long estudianteId, Long cursoId);
    List<InscripcionACursoDTO> listarInscripcionesPorEstudiante(Long estudianteId);
}
