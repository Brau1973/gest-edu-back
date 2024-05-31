package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/inscripcionCurso")
@Tag(name = "InscripcioCurso", description = "Inscripcion a Cursos")
public class InscripcionCursoController {
    private final InscripcionCursoService inscripcionCursoService;

    @Autowired
    public InscripcionCursoController(InscripcionCursoService inscripcionCursoService) {
        this.inscripcionCursoService = inscripcionCursoService;
    }

    @Operation(summary = "Inscribirse a un Curso")
    @PostMapping()
    //PreAuthorize("hasAuthority('ROL_ESTUDIANTE')")
    public ResponseEntity<InscripcionCursoDTO> registerCurso(@RequestBody InscripcionCursoDTO inscripcionCursoDTO) throws ParseException {
        InscripcionCursoDTO createdInscripcionCurso = inscripcionCursoService.createInscripcionCurso(inscripcionCursoDTO);

        return ResponseEntity.ok().body(createdInscripcionCurso);
    }
}
