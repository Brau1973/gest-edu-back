package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.curso.ActaCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.services.interfaces.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "API para operaciones de cursos")
public class CursoController {
    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @Operation(summary = "Registrar Curso de Asignatura")
    @PostMapping()
    @PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<CursoDTO> registerCurso(@RequestBody CursoDTO cursoDTO) throws ParseException {
        CursoDTO createdCurso = cursoService.createCurso(cursoDTO);

        return ResponseEntity.ok().body(createdCurso);
    }

    @Operation(summary = "Registrar Curso de Asignatura")
    @PostMapping("/{cursoId}/horarios")
    @PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<HorarioDTO> addHorarioToCurso(@PathVariable Long cursoId, @RequestBody HorarioDTO nuevoHorario) {
        HorarioDTO horario = cursoService.addHorarioToCurso(cursoId, nuevoHorario);
        return ResponseEntity.ok(horario);
    }

    @Operation(summary = "Listar Estudiantes de un Curso")
    @GetMapping("/{cursoId}/estudiantes")
    @PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<List<UsuarioDTO>> getEstudiantesByCursoId(@PathVariable Long cursoId) {
        List<UsuarioDTO> estudiantes = cursoService.getEstudiantesByCurso(cursoId);
        return ResponseEntity.ok(estudiantes);
    }

    @Operation(summary = "Obtener Curso mediante Id")
    @GetMapping("/{cursoId}")
    public ResponseEntity<CursoDTO> getCursoById(@PathVariable Long cursoId){
        CursoDTO curso = cursoService.getCursoPorId(cursoId);
        return  ResponseEntity.ok(curso);
    }

    @Operation(summary = "Obtener Horarios del Curso")
    @GetMapping("/{cursoId}/horarios")
    public ResponseEntity<Page<HorarioDTO>> getHorariosByCursoId(@PathVariable Long cursoId, Pageable pageable){
        Page<HorarioDTO> horarios = cursoService.getHorariosByCurso(cursoId, pageable);
        return ResponseEntity.ok(horarios);
    }

    @Operation(summary = "Obtener calificaciones de curso")
    @GetMapping("/{id}/calificaciones")
    @PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<List<InscripcionCursoCalificacionDTO>> getCalificacionesExamen(@PathVariable Long id) {
        List<InscripcionCursoCalificacionDTO> calificaciones = cursoService.obtenerCalificaciones(id);
        return ResponseEntity.ok().body(calificaciones);
    }

    @Operation(summary = "Generar acta de fin de Curso")
    @GetMapping("/{id}/acta")
    @PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<ActaCursoDTO> getActaCurso(@PathVariable Long id) {
        ActaCursoDTO actaCurso = cursoService.generarActaCurso(id);
        return ResponseEntity.ok().body(actaCurso);
    }
}
