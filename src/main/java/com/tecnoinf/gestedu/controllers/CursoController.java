package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.services.interfaces.CursoService;
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
    public ResponseEntity<CursoDTO> registerCurso(@RequestBody CursoDTO cursoDTO) throws ParseException {
        CursoDTO createdCurso = cursoService.createCurso(cursoDTO);

        return ResponseEntity.ok().body(createdCurso);
    }

    @Operation(summary = "Registrar Curso de Asignatura")
    @PostMapping("/{cursoId}/horarios")
    //PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<HorarioDTO> addHorarioToCurso(@PathVariable Long cursoId, @RequestBody HorarioDTO nuevoHorario) {
        HorarioDTO horario = cursoService.addHorarioToCurso(cursoId, nuevoHorario);
        return ResponseEntity.ok(horario);
    }
}
