package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.services.AsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignaturas")
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

}