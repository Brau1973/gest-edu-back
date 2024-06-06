package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.ActividadDTO;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actividades")
@Tag(name = "Actividad", description = "API para operaciones de actividades")
public class ActividadController {

    private final ActividadService actividadService;

    @Autowired
    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    //@PreAuthorize("hasAuthority('ROL_ADMINISTRADOR')")
    @Operation(summary = "Obtener la actividad de un usuario por su id")
    @GetMapping()
    public ResponseEntity<List<ActividadDTO>> getActividadByUsuarioId(@PathVariable Long id) {
        List<ActividadDTO> actividades = actividadService.getActividadByUsuarioId(id);
        return ResponseEntity.ok().body(actividades);
    }
}