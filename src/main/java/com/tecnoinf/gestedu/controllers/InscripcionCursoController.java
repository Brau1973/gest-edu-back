package com.tecnoinf.gestedu.controllers;

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
    public ResponseEntity<InscripcionCursoDTO> registerCurso(@RequestBody InscripcionCursoDTO inscripcionCursoDTO) throws ParseException {
        InscripcionCursoDTO createdInscripcionCurso = inscripcionCursoService.createInscripcionCurso(inscripcionCursoDTO);

        return ResponseEntity.ok().body(createdInscripcionCurso);
    }

    @Operation(summary = "Registrar calificaciones del curso")
    @PutMapping("/{id}/calificar")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<List<InscripcionCursoCalificacionDTO>> registrarCalificaciones(@PathVariable Long id, @RequestBody List<InscripcionCursoCalificacionDTO> calificaciones) {
        List<InscripcionCursoCalificacionDTO> calificacionesCurso = inscripcionCursoService.registrarCalificaciones(id, calificaciones);
        return ResponseEntity.ok().body(calificacionesCurso);
    }
}
