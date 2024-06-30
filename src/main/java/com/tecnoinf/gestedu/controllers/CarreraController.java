package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoHorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carreras")
@Tag(name = "Carreras", description = "API para operaciones de Carreras")
public class CarreraController {

    private final CarreraService carreraService;

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @Operation(summary = "Obtener la info basica de todas las carreras")
    @GetMapping()
    public ResponseEntity<Page<BasicInfoCarreraDTO>> getAllCarreras(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {
                Page<BasicInfoCarreraDTO> carreras = carreraService.getAllCarreras(pageable,nombre);
                return ResponseEntity.ok().body(carreras);
    }

    @Operation(summary = "Obtener la info basica de una carrera por su id")
    @GetMapping("/{id}")
    public ResponseEntity<BasicInfoCarreraDTO> getCarreraBasicInfoById(@PathVariable("id") Long id) {
        BasicInfoCarreraDTO carrera = carreraService.getCarreraBasicInfoById(id);
        return ResponseEntity.ok().body(carrera);
    }

    @Operation(summary = "Crear una carrera con su info basica")
    @PostMapping()
    @PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<CreateCarreraDTO> createCarrera(@RequestBody CreateCarreraDTO createCarreraDto) {
        CreateCarreraDTO createdCarrera = carreraService.createCarrera(createCarreraDto);
        return ResponseEntity.ok().body(createdCarrera);
    }

    @Operation(summary = "Actualizar info basica de una carrera")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<BasicInfoCarreraDTO> updateCarrera(@PathVariable Long id, @RequestBody CreateCarreraDTO createCarreraDTO) {
        BasicInfoCarreraDTO updatedCarrera = carreraService.updateCarrera(id, createCarreraDTO);
        return ResponseEntity.ok().body(updatedCarrera);
    }

    @Operation(summary = "Eliminar una carrera")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<?> deleteCarrera(@PathVariable Long id) {
        carreraService.deleteCarrera(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener las carreras sin plan de estudio")
    @GetMapping("/carreras-sin-plan-de-estudio")
    @PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<Page<BasicInfoCarreraDTO>> getCarrerasSinPlanDeEstudio(Pageable pageable) {
        Page<BasicInfoCarreraDTO> carreras = carreraService.getCarrerasSinPlanDeEstudio(pageable);
        return ResponseEntity.ok().body(carreras);
    }

    @Operation(summary = "Obtener las asignaturas de una carrera por su id (contieen info de plan de estudio si existe el mismo)")
    @GetMapping("/{id}/asignaturas")
    public ResponseEntity<Page<AsignaturaDTO>> getAsignaturasFromCarrera(@PathVariable Long id, Pageable pageable) {
        Page<AsignaturaDTO> asignaturas = carreraService.getAsignaturasFromCarrera(id, pageable);
        return ResponseEntity.ok().body(asignaturas);
    }

    @Operation(summary = "Actualiza el semestre en que recomienda el plan de estudio cursar las asignaturas de una carrera")
    @PutMapping("/{id}/asignaturas/semestre-plan-estudio")
    @PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    public ResponseEntity<?> updateSemestrePlanEstudio(@PathVariable Long id, @RequestBody List<AsignaturaDTO> asignaturasDto) {
        carreraService.updateSemestrePlanEstudio(id,asignaturasDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar estudiantes inscriptos a carrera")
    @GetMapping("/{id}/estudiantes-inscriptos")
    @PreAuthorize("hasAnyAuthority('ROL_COORDINADOR', 'ROL_FUNCIONARIO')")
    public ResponseEntity<List<InscripcionCarreraDTO>> getEstudiantesInscriptos(@PathVariable Long id) {
        List<InscripcionCarreraDTO> inscripciones = carreraService.getEstudiantesInscriptos(id);
        return ResponseEntity.ok().body(inscripciones);
    }

    @Operation(summary = "Listar periodos de examen de carrera")
    @GetMapping("/{id}/periodos-examen")
    @PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<Page<PeriodoExamenDTO>> obtenerPeriodosExamenCarrera(@PathVariable Long id, Pageable pageable) {
        Page<PeriodoExamenDTO> periodosExamen = carreraService.obtenerPeriodosExamenCarrera(id, pageable);
        return ResponseEntity.ok().body(periodosExamen);
    }

    @Operation(summary = "Obtener asignaturas de la carrera con exámenes activos")
    @GetMapping("/{id}/asignaturas-con-examenes-activos")
    public ResponseEntity<List<AsignaturaDTO>> obtenerAsignaturasConExamenesActivos(@PathVariable Long id) {
        List<AsignaturaDTO> asignaturas = carreraService.obtenerAsignaturasConExamenesActivos(id);
        return ResponseEntity.ok().body(asignaturas);
    }

    @Operation(summary = "Obtener cursos activos de Carrera")
    @GetMapping("/{idCarrera}/cursos-activos")
    public ResponseEntity<List<CursoDTO>> obtenerCursosActivosCarrera(@PathVariable Long idCarrera) {
        List<CursoDTO> cursos = carreraService.obtenerCursosActivos(idCarrera);
        return ResponseEntity.ok().body(cursos);
    }

    @Operation(summary = "Listar Horarios de Cursos por Carrera")
    @GetMapping("/{idCarrera}/horarios-cursos")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<Page<CursoHorarioDTO>> obtenerHorariosCursosCarrera(@PathVariable Long idCarrera, Pageable pageable){
        Page<CursoHorarioDTO> cursosHorarios = carreraService.obtenerHorariosCursosCarrera(idCarrera, pageable);
        return ResponseEntity.ok().body(cursosHorarios);
    }
}