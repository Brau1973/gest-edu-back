package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.enumerados.Estado;
import com.tecnoinf.gestedu.services.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<CursoDTO> registerHorarioCurso(@RequestBody HorarioCursoDTO curso) throws ParseException {
        CursoDTO nuevoCurso = new CursoDTO();
        nuevoCurso.setId(curso.getIdCurso());
        nuevoCurso.setFechaInicio(curso.getFechaInicio());
        nuevoCurso.setFechaFin(curso.getFechaFin());
        nuevoCurso.setEstado(Estado.ACTIVO);
        nuevoCurso.setDiasPrevInsc(curso.getDiasPrevInsc());
        nuevoCurso.setAsignaturaId(curso.getAsignaturaId());
        HorarioDTO nuevoHorario = new HorarioDTO();
        nuevoHorario.setId(curso.getIdHorario());
        nuevoHorario.setDia(curso.getDia());
        nuevoHorario.setHoraInicio(curso.getHoraInicio());
        nuevoHorario.setHoraFin(curso.getHoraFin());
        Long docente = curso.getIdDocente();
        CursoDTO createdCurso = cursoService.createCurso(nuevoCurso, nuevoHorario, docente);
        return ResponseEntity.ok().body(createdCurso);
    }
}
