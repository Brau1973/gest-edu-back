package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.exceptions.FechaException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.PeriodoExamen;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.PeriodoExamenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoExamenServiceImpl implements PeriodoExamenService{

    @Autowired
    CarreraRepository carreraRepository;

    @Autowired
    PeriodoExamenRepository periodoExamenRepository;

    @Autowired
    ModelMapper modeloMapper;

    @Override
    public PeriodoExamenDTO registrarPeriodoExamen(PeriodoExamenDTO periodoExamenDTO) {
        PeriodoExamen periodoExamen = new PeriodoExamen();
        LocalDateTime fechaInicio = periodoExamenDTO.getFechaInicio().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fechaFin = periodoExamenDTO.getFechaFin().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        if(fechaInicio.isAfter(fechaFin)){
            throw new FechaException("La fecha de inicio no puede ser mayor a la fecha de fin");
        }
        if(fechaFin.isBefore(LocalDateTime.now())){
            throw new FechaException("La fecha de fin no puede ser menor a la fecha actual");
        }
        Optional<PeriodoExamen> existePeriodoExamenCarrera = periodoExamenRepository.findByFechaInicioAndFechaFinAndCarreraId(fechaInicio, fechaFin, periodoExamenDTO.getCarreraid());
        if (existePeriodoExamenCarrera.isPresent()) {
            throw new UniqueFieldException("Ya existe un período de examen con el mismo período para la carrera" + periodoExamenDTO.getCarreraid());
        }

        periodoExamen.setFechaInicio(fechaInicio);
        periodoExamen.setFechaFin(fechaFin);
        Optional<Carrera> carrera = carreraRepository.findById(periodoExamenDTO.getCarreraid());
        if(carrera.isEmpty()){
            throw new ResourceNotFoundException("Carrera no encontrada");
        }
        periodoExamen.setCarrera(carrera.get());
        periodoExamenRepository.save(periodoExamen);

        PeriodoExamenDTO nuevo = new PeriodoExamenDTO();
        nuevo.setCarreraid(periodoExamen.getCarrera().getId());
        nuevo.setFechaInicio(periodoExamen.getFechaInicio());
        nuevo.setFechaFin(periodoExamen.getFechaFin());
        nuevo.setId(periodoExamen.getId());
        return nuevo;
    }
}
