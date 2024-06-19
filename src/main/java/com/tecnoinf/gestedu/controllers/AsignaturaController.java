package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.services.interfaces.AsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignaturas")
@Tag(name = "Asignaturas", description = "API para operaciones de Asignaturas")
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @Autowired
    public AsignaturaController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }

    @Operation(summary = "Crear una asignatura")
    @PostMapping()
    //@PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<AsignaturaDTO> createAsignatura(@RequestBody CreateAsignaturaDTO createAsignaturaDto) {
        AsignaturaDTO createdAsignatura = asignaturaService.createAsignatura(createAsignaturaDto);
        return ResponseEntity.ok().body(createdAsignatura);
    }

    @Operation(summary = "Actualizar info basica de una asignatura")
    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<AsignaturaDTO> updateAsignatura(@PathVariable Long id, @RequestBody CreateAsignaturaDTO createCarreraDTO) {
        AsignaturaDTO updatedAsignatura = asignaturaService.updateAsignatura(id, createCarreraDTO);
        return ResponseEntity.ok().body(updatedAsignatura);
    }

    @Operation(summary = "Obtener previas de una asignatura")
    @GetMapping("/{asignaturaId}/previas")
    public ResponseEntity<List<AsignaturaDTO>> getPrevias(@PathVariable Long asignaturaId) {
        List<AsignaturaDTO> previas = asignaturaService.getPrevias(asignaturaId);
        return ResponseEntity.ok().body(previas);
    }

    @Operation(summary = "Obtener asignaturas NO previas de una asignatura")
    @GetMapping("/{asignaturaId}/no-previas")
    public ResponseEntity<List<AsignaturaDTO>> getNoPrevias(@PathVariable Long asignaturaId) {
        List<AsignaturaDTO> noPrevias = asignaturaService.getNoPrevias(asignaturaId);
        return ResponseEntity.ok().body(noPrevias);
    }

    @Operation(summary = "Registrar previa de una asignatura")
    @PostMapping("/{asignaturaId}/previa/{previaId}")
    //@PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<AsignaturaDTO> addPrevia(@PathVariable Long asignaturaId, @PathVariable Long previaId) {
        AsignaturaDTO updatedAsignatura = asignaturaService.addPrevia(asignaturaId, previaId);
        return ResponseEntity.ok().body(updatedAsignatura);
    }

    @Operation(summary = "Obtener asignatura por id")
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> getAsignaturaById(@PathVariable("id") Long id) {
        AsignaturaDTO asignatura = asignaturaService.getAsignaturaById(id);
        return ResponseEntity.ok().body(asignatura);
    }

    @Operation(summary = "Obtener examenes de una asignatura")
    @GetMapping("/{asignaturaId}/examenes")
    public ResponseEntity<Page<ExamenDTO>> getExamenes(@PathVariable Long asignaturaId, Pageable pageable) {
        Page<ExamenDTO> examenes = asignaturaService.obtenerExamenes(asignaturaId, pageable);
        return ResponseEntity.ok().body(examenes);
    }

    @Operation(summary = "Obtener examenes de una asignatura fuera de fecha de inscripcion, sin calificar [activos]")
    @GetMapping("/{asignaturaId}/examenesSinCalificar")
    public ResponseEntity<Page<ExamenDTO>> getExamenesFueraInscripcionSinCalificar(@PathVariable Long asignaturaId, Pageable pageable) {
        Page<ExamenDTO> examenes = asignaturaService.obtenerExamenesFueraInscripcionSinCalificar(asignaturaId, pageable);
        return ResponseEntity.ok().body(examenes);
    }

    @Operation(summary = "Obtener examenes de una asignatura en fecha de inscripcion")
    @GetMapping("/{asignaturaId}/examenesVigentes")
    public ResponseEntity<Page<ExamenDTO>> getExamenesEnFechaInscripcion(@PathVariable Long asignaturaId, Pageable pageable) {
        Page<ExamenDTO> examenes = asignaturaService.obtenerExamenesEnFechaInscripcion(asignaturaId, pageable);
        return ResponseEntity.ok().body(examenes);
    }

    @Operation(summary = "Obtener Cursos de una Asignatura")
    @GetMapping("{asignaturaId}/cursos")
    public ResponseEntity<List<CursoDTO>> getCursosDeAsignatura(@PathVariable Long asignaturaId){
        List<CursoDTO> cursos = asignaturaService.obtenerCursosDeAsignatura(asignaturaId);
        return ResponseEntity.ok().body(cursos);
    }

    @Operation(summary = "Obtener cursos calificados de una asignatura")
    @GetMapping("{asignaturaId}/cursosCalificados")
    public ResponseEntity<List<CursoDTO>> getCursosCalificadosDeAsignatura(@PathVariable Long asignaturaId){
        List<CursoDTO> cursos = asignaturaService.obtenerCursosCalificadosDeAsignatura(asignaturaId);
        return ResponseEntity.ok().body(cursos);
    }

    @Operation(summary = "Obtener examenes calificados de una asignatura")
    @GetMapping("{asignaturaId}/examenesCalificados")
    public ResponseEntity<List<ExamenDTO>> getExamenesCalificadosDeAsignatura(@PathVariable Long asignaturaId){
        List<ExamenDTO> examenes = asignaturaService.obtenerExamenesCalificadosDeAsignatura(asignaturaId);
        return ResponseEntity.ok().body(examenes);
    }
}