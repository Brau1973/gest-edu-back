package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarreraService {
    Page<BasicInfoCarreraDTO> getAllCarreras(Pageable pageable, String nombre);
    BasicInfoCarreraDTO getCarreraBasicInfoById(Long id);
    CreateCarreraDTO createCarrera(CreateCarreraDTO createCarreraDto);
    BasicInfoCarreraDTO updateCarrera(Long id, BasicInfoCarreraDTO basicInfoCarreraDTO);
    void deleteCarrera(Long id);
}