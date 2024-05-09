package com.tecnoinf.gestedu.controllers;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.services.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docentes")
public class DocenteController {

    private final DocenteService docenteService;

    @Autowired
    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    public ResponseEntity<Page<DocenteDTO>> getAllDocentes(Pageable pageable,
                                                           @RequestParam(required = false) String documento,
                                                           @RequestParam(required = false) String nombre,
                                                           @RequestParam(required = false) String apellido) {
        return ResponseEntity.ok(docenteService.getAllDocentes(pageable, documento, nombre, apellido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> getDocenteById(@PathVariable Long id) {
        return ResponseEntity.ok(docenteService.getDocenteById(id));
    }

    @PostMapping
    public ResponseEntity<DocenteDTO> createDocente(@RequestBody DocenteDTO docenteDto) {
        return ResponseEntity.ok(docenteService.createDocente(docenteDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteDTO> updateDocente(@PathVariable Long id, @RequestBody DocenteDTO docenteDto) {
        return ResponseEntity.ok(docenteService.updateDocente(id, docenteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocente(@PathVariable Long id) {
        docenteService.deleteDocente(id);
        return ResponseEntity.ok().build();
    }
}