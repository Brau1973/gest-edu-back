package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.curso.CursoHorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/inscripcionCurso")
@Tag(name = "InscripcioCurso", description = "Inscripcion a Cursos")
public class InscripcionCursoController {
    private final InscripcionCursoService inscripcionCursoService;

    @Autowired
    public InscripcionCursoController(InscripcionCursoService inscripcionCursoService) {
        this.inscripcionCursoService = inscripcionCursoService;
    }

    @Operation(summary = "Inscribirse a un Curso")
    @PostMapping()
    //PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<InscripcionCursoDTO> createInscripcionCurso(@RequestBody InscripcionCursoDTO inscripcionCursoDTO) throws ParseException {
        InscripcionCursoDTO createdInscripcionCurso = inscripcionCursoService.createInscripcionCurso(inscripcionCursoDTO);

        return ResponseEntity.ok().body(createdInscripcionCurso);
    }

    @Operation(summary = "Registrar calificaciones del curso")
    @PutMapping("/{cursoId}/calificar")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<List<InscripcionCursoCalificacionDTO>> registrarCalificaciones(@PathVariable Long cursoId, @RequestBody List<InscripcionCursoCalificacionDTO> calificaciones) {
        List<InscripcionCursoCalificacionDTO> calificacionesCurso = inscripcionCursoService.registrarCalificaciones(cursoId, calificaciones);
        return ResponseEntity.ok().body(calificacionesCurso);
    }

    @Operation(summary = "Cancelar Inscripción a un Curso")
    @DeleteMapping("/cancelarInscripcion/{inscripcionCursoId}")
    //@PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<Void> cancelInscripcionCurso(@PathVariable Long inscripcionCursoId) {
        inscripcionCursoService.deleteInscripcionCurso(inscripcionCursoId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    @Operation(summary = "Listar Cursos y Horarios de Estudiante Inscripto al Curso")
    @GetMapping("/{idEstudiante}/cursos-inscripto")
    //@PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<List<CursoHorarioDTO>> getCursosHorariosInscriptos(@PathVariable Long idEstudiante){
        List<CursoHorarioDTO> cursos = inscripcionCursoService.listarCursosHorariosInscriptos(idEstudiante);
        return ResponseEntity.ok().body(cursos);
    }
}