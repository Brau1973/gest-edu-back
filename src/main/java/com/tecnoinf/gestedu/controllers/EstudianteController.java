package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.services.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/estudiantes")
@Tag(name = "Estudiantes", description = "API para operaciones de Estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @Operation(summary = "Obtiene las carreras que tiene plan de estudio y que el estudiante no esta inscripto")
    @GetMapping("/carreras-no-inscripto")
    public ResponseEntity<Page<Carrera>> getCarrerasNoInscripto(Principal principal, Pageable pageable) {
        String email = principal.getName();
        Page<Carrera> page = estudianteService.getCarrerasNoInscripto(email, pageable);
        return ResponseEntity.ok(page);
    }
}