package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.models.PeriodoExamen;
import com.tecnoinf.gestedu.services.PeriodoExamenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/periodoExamen")
@Tag(name = "PeriodoExamen", description = "API para operaciones de PeriodoExamen")
public class PeriodoExamenController {

    @Autowired
    PeriodoExamenService periodoExamenService;

    @Operation(summary = "Registrar periodo de examen")
    @PostMapping("/registrar")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<PeriodoExamenDTO> registrarPeriodoExamen(@RequestBody PeriodoExamenDTO periodoExamenDTO) {
        PeriodoExamenDTO nuevo = periodoExamenService.registrarPeriodoExamen(periodoExamenDTO);
        return ResponseEntity.ok(nuevo);
    }
}
