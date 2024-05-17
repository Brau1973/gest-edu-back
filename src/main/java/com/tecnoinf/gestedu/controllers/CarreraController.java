package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.services.CarreraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    //TODO Agrergar endpoint para obtener todas las asignaturas de una carrera

    // TODO Agregar endpoint para obtener la info basica de la carrera junto con todas sus asignaturas

    @Operation(summary = "Crear una carrera con su info basica")
    @PostMapping()
    public ResponseEntity<CreateCarreraDTO> createCarrera(@RequestBody CreateCarreraDTO createCarreraDto) {
        CreateCarreraDTO createdCarrera = carreraService.createCarrera(createCarreraDto);
        return ResponseEntity.ok().body(createdCarrera);
    }

    @Operation(summary = "Actualizar info basica de una carrera")
    @PutMapping("/{id}")
    public ResponseEntity<BasicInfoCarreraDTO> updateCarrera(@PathVariable Long id, @RequestBody BasicInfoCarreraDTO basicInfoCarreraDTO) {
        BasicInfoCarreraDTO updatedCarrera = carreraService.updateCarrera(id, basicInfoCarreraDTO);
        return ResponseEntity.ok().body(updatedCarrera);
    }

    @Operation(summary = "Eliminar una carrera")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarrera(@PathVariable Long id) {
        carreraService.deleteCarrera(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener las carreras sin plan de estudio")
    @GetMapping("/carreras-sin-plan-de-estudio")
    public ResponseEntity<Page<BasicInfoCarreraDTO>> getCarrerasSinPlanDeEstudio(Pageable pageable) {
        Page<BasicInfoCarreraDTO> carreras = carreraService.getCarrerasSinPlanDeEstudio(pageable);
        return ResponseEntity.ok().body(carreras);
    }

    @Operation(summary = "Obtener las asignaturas de una carrera por su id")
    @GetMapping("/{id}/asignaturas")
    public ResponseEntity<Page<AsignaturaDTO>> getAsignaturasFromCarrera(@PathVariable Long id, Pageable pageable) {
        Page<AsignaturaDTO> asignaturas = carreraService.getAsignaturasFromCarrera(id, pageable);
        return ResponseEntity.ok().body(asignaturas);
    }

    @Operation(summary = "Actualiza el semestre en que recomienda el plan de estudio cursar las asignaturas de una carrera")
    @PutMapping("/{id}/asignaturas/semestre-plan-estudio")
    public ResponseEntity<?> updateSemestrePlanEstudio(@PathVariable Long id, @RequestBody List<AsignaturaDTO> asignaturasDto) {
        carreraService.updateSemestrePlanEstudio(id,asignaturasDto);
        return ResponseEntity.ok().build();
    }

}