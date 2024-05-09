package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.services.CarreraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carreras")
@Tag(name = "Carreras", description = "API para operaciones de Carreras")
public class CarreraController {

    CarreraService carreraService;

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @Operation(summary = "Obtener todas las carreras")
    @GetMapping()
    public ResponseEntity<Page<CreateCarreraDTO>> getAllCarreras(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {
                Page<CreateCarreraDTO> carreras = carreraService.getAllCarreras(pageable,nombre);
                return ResponseEntity.ok().body(carreras);
    }

    @Operation(summary = "Obtener una carrera por su id")
    @GetMapping("/{id}")
    public ResponseEntity<CreateCarreraDTO> getCarreraById(@PathVariable("id") Long id) {
        CreateCarreraDTO carrera = carreraService.getCarreraById(id);
        return ResponseEntity.ok().body(carrera);
    }

    @Operation(summary = "Crear una carrera")
    @PostMapping()
    public ResponseEntity<CreateCarreraDTO> createCarrera(@RequestBody CreateCarreraDTO createCarreraDto) {
        CreateCarreraDTO createdCarrera = carreraService.createCarrera(createCarreraDto);
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//                .buildAndExpand(createdCarrera.getId();
//        return ResponseEntity.created(location).body(createdCarrera);
        return ResponseEntity.ok().body(createdCarrera);
    }

    @Operation(summary = "Actualizar una carrera")
    @PutMapping("/{id}")
    public ResponseEntity<CreateCarreraDTO> updateCarrera(@PathVariable Long id, @RequestBody CreateCarreraDTO createCarreraDto) {
        CreateCarreraDTO updatedCarrera = carreraService.updateCarrera(id, createCarreraDto);
        return ResponseEntity.ok().body(updatedCarrera);
    }

    @Operation(summary = "Eliminar una carrera")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarrera(@PathVariable Long id) {
        carreraService.deleteCarrera(id);
        return ResponseEntity.noContent().build();
    }
}