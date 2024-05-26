package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.services.interfaces.TramiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/tramites")
@Tag(name = "Tramites", description = "API para operaciones de Tramites")
public class TramiteController {

    private final TramiteService tramiteService;

    @Autowired
    public TramiteController(TramiteService tramiteService) {
        this.tramiteService = tramiteService;
    }

    @Operation(summary = "Crear un nuevo trámite (inscripción a una carrera o solicitud de titulo)")
    @PostMapping("/nuevo-tramite")
    public ResponseEntity<TramiteDTO> nuevoTramite(@RequestParam Long carreraId, @Parameter(example = "INSCRIPCION_A_CARRERA") @RequestParam TipoTramite tipoTramite, Principal principal) throws MessagingException {
        String email = (principal != null) ? principal.getName() : "estudianteInitData@yahoo.com";
        return ResponseEntity.ok().body(tramiteService.nuevoTramite(carreraId, tipoTramite, email));
    }
}