package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.NotificacionDTO;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import com.tecnoinf.gestedu.services.interfaces.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificaciones", description = "API para operaciones de Notificaciones")
public class NotificacionController {

    private final EstudianteService estudianteService;
    private final NotificacionService notificacionService;

    @Autowired
    public NotificacionController(EstudianteService estudianteService, NotificacionService notificacionService) {
        this.estudianteService = estudianteService;
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Registrar token Firebase de dispositivo para notificaciones push")
    @PostMapping("/tokenFirebase")
    public ResponseEntity<?> asignarTokenFirebase(Principal principal, @RequestBody String tokenFirebase) {
        notificacionService.registrarTokenFirebase(principal.getName(), tokenFirebase);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Marcar notificaciones como leidas")
    @PostMapping("/{idNotificacion}/leida")
    public ResponseEntity<?> marcarNotificacionesLeidas(Principal principal, @PathVariable Long idNotificacion) {
        notificacionService.marcarNotificacionLeida(principal.getName(), idNotificacion);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener notificaciones de un estudiante")
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(Principal principal) {
        List<NotificacionDTO> notificaciones = notificacionService.obtenerNotificaciones(principal.getName());
        if(notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(notificaciones);
        }
    }

    @Operation(summary = "Obtener cantidad de notificaciones no leidas")
    @GetMapping("/cantNoLeidas")
    public ResponseEntity<Integer> obtenerCantidadNotificacionesNoLeidas(Principal principal) {
        return ResponseEntity.ok(notificacionService.obtenerCantidadNotificacionesNoLeidas(principal.getName()));
    }

}