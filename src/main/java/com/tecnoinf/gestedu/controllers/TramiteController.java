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
import org.springframework.security.access.prepost.PreAuthorize;
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

    //@PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    @Operation(summary = "Crear un nuevo trámite (inscripción a una carrera o solicitud de titulo)")
    @PostMapping("/nuevo-tramite")
    public ResponseEntity<TramiteDTO> nuevoTramite(@RequestParam Long carreraId, @Parameter(example = "INSCRIPCION_A_CARRERA") @RequestParam TipoTramite tipoTramite, Principal principal) throws MessagingException {
        String email = (principal != null) ? principal.getName() : "estudiante1InitData@yahoo.com";
        return ResponseEntity.ok().body(tramiteService.nuevoTramite(carreraId, tipoTramite, email));
    }

    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    @Operation(summary = "Listar todos los trámites de inscripción a carrera PENDIENTES")
    @GetMapping("/inscripcion-carrera-pendientes")
    public ResponseEntity<?> listarTramitesInscripcionCarreraPendientes() {
        return ResponseEntity.ok().body(tramiteService.listarTramitesInscripcionCarreraPendientes());
    }

    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    @Operation(summary = "Aprobar un trámite de inscripción a carrera")
    @PutMapping("/aprobar-tramite-inscripcion-carrera/{tramiteId}")
    public ResponseEntity<TramiteDTO> aprobarTramiteInscripcionCarrera(@PathVariable Long tramiteId, Principal principal) throws MessagingException {
        String email = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok().body(tramiteService.aprobarTramiteInscripcionCarrera(tramiteId, email));
    }

    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    @Operation(summary = "Rechazar un trámite de inscripción a carrera")
    @PutMapping("/rechazar-tramite-inscripcion-carrera/{tramiteId}")
    public ResponseEntity<TramiteDTO> rechazarTramiteInscripcionCarrera(@PathVariable Long tramiteId, @RequestBody String motivoRechazo, Principal principal) throws MessagingException {
        String email = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok().body(tramiteService.rechazarTramiteInscripcionCarrera(tramiteId, email, motivoRechazo));
    }

    //@PreAuthorize("hasAuthority('ROL_COORDINADOR')")
    @Operation(summary = "Listar todos los trámites de solicitud de titulo PENDIENTES")
    @GetMapping("/solicitud-titulo-pendientes")
    public ResponseEntity<?> listarTramitesSolicitudTituloPendientes() {
        return ResponseEntity.ok().body(tramiteService.listarTramitesSolicitudTituloPendientes());
    }

    @Operation(summary = "Aprobar un trámite de solicitud de titulo")
    @PutMapping("/aprobar-tramite-solicitud-titulo/{tramiteId}")
    public ResponseEntity<TramiteDTO> aprobarTramiteSolicitudTitulo(@PathVariable Long tramiteId, Principal principal) throws MessagingException {
        String email = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok().body(tramiteService.aprobarTramiteSolicitudTitulo(tramiteId, email));
    }

    @Operation(summary = "Rechazar un trámite de solicitud de titulo")
    @PutMapping("/rechazar-tramite-solicitud-titulo/{tramiteId}")
    public ResponseEntity<TramiteDTO> rechazarTramiteSolicitudTitulo(@PathVariable Long tramiteId, @RequestBody String motivoRechazo, Principal principal) throws MessagingException {
        String email = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok().body(tramiteService.rechazarTramiteSolicitudTitulo(tramiteId, email, motivoRechazo));
    }

}