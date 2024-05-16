package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.services.AsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @Operation(summary = "Crear una asignatura")
    @PostMapping()
    public ResponseEntity<AsignaturaDTO> createAsignatura(@RequestBody CreateAsignaturaDTO createAsignaturaDto) {
        AsignaturaDTO createdAsignatura = asignaturaService.createAsignatura(createAsignaturaDto);
        return ResponseEntity.ok().body(createdAsignatura);
    }
}