package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.enumerados.Estado;
import com.tecnoinf.gestedu.services.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @Operation(summary = "Registrar Curso de Asignatura")
    @PostMapping()
    //PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<CursoDTO> registerHorarioCurso(@RequestBody HorarioCursoDTO horarioCursoDTO) throws ParseException {
        CursoDTO nuevoCurso = new CursoDTO();
        nuevoCurso.setId(horarioCursoDTO.getIdCurso());
        nuevoCurso.setFechaInicio(horarioCursoDTO.getFechaInicio());
        nuevoCurso.setFechaFin(horarioCursoDTO.getFechaFin());
        nuevoCurso.setEstado(Estado.ACTIVO);
        nuevoCurso.setDiasPrevInsc(horarioCursoDTO.getDiasPrevInsc());
        nuevoCurso.setAsignaturaId(horarioCursoDTO.getAsignaturaId());
        HorarioDTO nuevoHorario = new HorarioDTO();
        nuevoHorario.setId(horarioCursoDTO.getIdHorario());
        nuevoHorario.setDia(horarioCursoDTO.getDia());
        nuevoHorario.setHoraInicio(horarioCursoDTO.getHoraInicio());
        nuevoHorario.setHoraFin(horarioCursoDTO.getHoraFin());
        Long idDocente = horarioCursoDTO.getIdDocente();
        CursoDTO createdCurso = cursoService.createCurso(nuevoCurso, nuevoHorario, idDocente);

        return ResponseEntity.ok().body(createdCurso);
    }
}
