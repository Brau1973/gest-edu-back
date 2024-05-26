package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarreraService {
    Page<BasicInfoCarreraDTO> getAllCarreras(Pageable pageable, String nombre);
    BasicInfoCarreraDTO getCarreraBasicInfoById(Long id);
    CreateCarreraDTO createCarrera(CreateCarreraDTO createCarreraDto);
    BasicInfoCarreraDTO updateCarrera(Long id, BasicInfoCarreraDTO basicInfoCarreraDTO);
    void deleteCarrera(Long id);
    Page<BasicInfoCarreraDTO> getCarrerasSinPlanDeEstudio(Pageable pageable);
    Page<AsignaturaDTO> getAsignaturasFromCarrera(Long id, Pageable pageable);
    void updateSemestrePlanEstudio(Long id, List<AsignaturaDTO> asignaturasDto);
}