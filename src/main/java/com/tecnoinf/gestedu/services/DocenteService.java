package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocenteService {
    Page<DocenteDTO> getAllDocentes(Pageable pageable,  String documento, String nombre, String apellido);
    DocenteDTO getDocenteById(Long id);
    DocenteDTO createDocente(DocenteDTO docenteDto);
    DocenteDTO updateDocente(Long id, DocenteDTO docenteDto);
    void deleteDocente(Long id);
}
