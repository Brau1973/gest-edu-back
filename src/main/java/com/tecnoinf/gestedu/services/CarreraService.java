package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarreraService {
    Page<CreateCarreraDTO> getAllCarreras(Pageable pageable, String nombre);
    CreateCarreraDTO getCarreraById(Long id);
    CreateCarreraDTO createCarrera(CreateCarreraDTO createCarreraDto);
    CreateCarreraDTO updateCarrera(Long id, CreateCarreraDTO createCarreraDto);
    void deleteCarrera(Long id);
}