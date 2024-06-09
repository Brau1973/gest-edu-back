package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoHorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @GetMapping("/cursos-inscripto")
    //@PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<List<CursoHorarioDTO>> getCursosHorariosInscriptos(Principal principal){
        List<CursoHorarioDTO> cursos = inscripcionCursoService.listarCursosHorariosInscriptos(principal.getName());
        return ResponseEntity.ok().body(cursos);
    }

    @Operation(summary = "Darse de baja de un curso.")
    @DeleteMapping("/{inscripcionCursoId}/eliminar")
    //@PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<InscripcionCursoDTO> deleteCurso(@PathVariable Long inscripcionCursoId, Principal principal){
        if(principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        InscripcionCursoDTO bajaCurso = inscripcionCursoService.darseDeBajaCurso(inscripcionCursoId, principal.getName());
        return ResponseEntity.ok().body(bajaCurso);
    }

    @Operation(summary = "Listar cursos disponibles de asignatura")
    @GetMapping("/{idAsignatura}/cursos-disponibles")
    public ResponseEntity<List<CursoDTO>> getCursosDisponiblesInscripcion(@PathVariable Long idAsignatura, Principal principal){
        List<CursoDTO> cursosDisponibles = inscripcionCursoService.getCursosDisponibles(idAsignatura, principal.getName());
        return  ResponseEntity.ok().body(cursosDisponibles);
    }
}