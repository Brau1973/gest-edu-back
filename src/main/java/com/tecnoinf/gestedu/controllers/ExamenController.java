package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCarrera.InscripcionCarreraDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.CreateInscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import com.tecnoinf.gestedu.services.interfaces.ExamenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/examenes")
@Tag(name = "Examen", description = "API para la gestión de exámenes")
public class ExamenController {

    private final ExamenService examenService;

    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @Operation(summary = "Crear un examen")
    @PostMapping("/crear")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<ExamenDTO> createExamen(@RequestBody CreateExamenDTO createExamenDto) {
        ExamenDTO createdExamen = examenService.altaExamen(createExamenDto);
        return ResponseEntity.ok().body(createdExamen);
    }

    @Operation(summary = "Inscribirse a un examen")
    @PostMapping("/inscribirse")
    //@PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<InscripcionExamenDTO> inscribirseExamen(@RequestBody CreateInscripcionExamenDTO inscripcionExamenDto, Principal principal) {
        if(principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        inscripcionExamenDto.setEmail(principal.getName());
        InscripcionExamenDTO createdExamen = examenService.inscribirseExamen(inscripcionExamenDto);

        return ResponseEntity.ok().body(createdExamen);
    }

    @Operation(summary = "Listar estudiantes inscriptos a un examen")
    @GetMapping("/{id}/estudiantes-inscriptos")
    public ResponseEntity<List<InscripcionExamenDTO>> getEstudiantesInscriptos(@PathVariable Long id) {
        List<InscripcionExamenDTO> inscripciones = examenService.listarInscriptosExamen(id);
        return ResponseEntity.ok().body(inscripciones);
    }

    @Operation(summary = "Listar examenes pendientes de calificar")
    @GetMapping("/examenes-pendientes")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<Page<ExamenDTO>> listarExamenesPendientes(Pageable pageable) {
        return new ResponseEntity<>(examenService.listarExamenesPendientes(pageable), HttpStatus.OK);
    }
}