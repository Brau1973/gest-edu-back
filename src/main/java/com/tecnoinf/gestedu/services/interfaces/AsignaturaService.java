package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AsignaturaService {
    AsignaturaDTO createAsignatura(CreateAsignaturaDTO createAsignaturaDto);
    List<AsignaturaDTO> getPrevias(Long asignaturaId);
    AsignaturaDTO addPrevia(Long asignaturaId, Long previaId);
    List<AsignaturaDTO> getNoPrevias(Long asignaturaId);
    AsignaturaDTO getAsignaturaById(Long id);
    Page<ExamenDTO> obtenerExamenes(Long asignaturaId, Pageable pageable);
    Page<ExamenDTO> obtenerExamenesEnFechaInscripcion(Long asignaturaId, Pageable pageable);
    List<CursoDTO> obtenerCursosDeAsignatura(Long asignaturaId);

}
