package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import com.tecnoinf.gestedu.models.Carrera;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
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

    @Operation(summary = "Listar estudiantes")
    @GetMapping("/listar")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<Page<BasicInfoUsuarioDTO>> listarEstudiantes(Pageable pageable) {
        return new ResponseEntity<>(estudianteService.obtenerEstudiantes(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Obtiene las carreras que tienen plan de estudio y que el estudiante no esta inscripto")
    @GetMapping("/carreras-no-inscripto")
    public ResponseEntity<Page<BasicInfoCarreraDTO>> getCarrerasNoInscripto(Principal principal, Pageable pageable) {
        String email = principal.getName();
        Page<BasicInfoCarreraDTO> page = estudianteService.getCarrerasNoInscripto(email, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtiene las carreras que tienen plan de estudio y que el estudiante está inscripto")
    @GetMapping("/carreras-inscripto")
    public ResponseEntity<Page<BasicInfoCarreraDTO>> getCarrerasInscripto(Principal principal, Pageable pageable) {
        String email = principal.getName();
        Page<BasicInfoCarreraDTO> page = estudianteService.getCarrerasInscripto(email, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Buscar estudiante por ci")
    @GetMapping("/buscar/{ci}")
    //@PreAuthorize("hasAuthority('ROL_FUNCIONARIO')")
    public ResponseEntity<BasicInfoUsuarioDTO> buscarEstudiantePorCi(@PathVariable String ci) {
        Optional<BasicInfoUsuarioDTO> estudiante = estudianteService.obtenerEstudiantePorCi(ci);
        if(estudiante.isPresent()){
            return new ResponseEntity<>(estudiante.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Listar asignaturas de estudiante inscripto, con última calificación de curso A EXAMEN para una carrera")
    @GetMapping("/{carreraId}/asignaturas-a-examen")
    public ResponseEntity<Page<AsignaturaDTO>> getAsignaturasAExamen(@PathVariable Long carreraId, Principal principal, Pageable pageable) {
        String email = principal.getName();
        Page<AsignaturaDTO> page = estudianteService.obtenerAsignaturasAExamen(carreraId, email, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Listar asignaturas pendiente de aprobacion para finalizar carrera.")
    @GetMapping("/{carreraId}/asignaturas-pendientes")
    public ResponseEntity<Page<AsignaturaDTO>> getAsignaturasPendientes(@PathVariable Long carreraId, Principal principal, Pageable pageable) {
        Page<AsignaturaDTO> page = estudianteService.obtenerAsignaturasPendientes(carreraId, principal.getName(), pageable);
        return ResponseEntity.ok(page);
    }
}