package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.exceptions.FechaException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.PeriodoExamen;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.PeriodoExamenRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.PeriodoExamenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoExamenServiceImpl implements PeriodoExamenService {

    @Autowired
    CarreraRepository carreraRepository;

    @Autowired
    PeriodoExamenRepository periodoExamenRepository;

    @Autowired
    ModelMapper modeloMapper;

    @Autowired
    ActividadService actividadService;

    @Override
    public PeriodoExamenDTO registrarPeriodoExamen(PeriodoExamenDTO periodoExamenDTO) {

        LocalDate dateInicio = LocalDate.parse(periodoExamenDTO.getFechaInicio());
        LocalDateTime fechaInicio = dateInicio.atStartOfDay();

        LocalDate dateFin = LocalDate.parse(periodoExamenDTO.getFechaFin());
        LocalDateTime fechaFin = dateFin.atTime(23, 59, 59, 999999999);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        validarFechas(fechaInicio, fechaFin);
        verificarPeriodoExistente(fechaInicio, fechaFin, periodoExamenDTO.getCarreraid());

        periodoExamen.setFechaInicio(fechaInicio);
        periodoExamen.setFechaFin(fechaFin);
        periodoExamen.setCarrera(obtenerCarrera(periodoExamenDTO.getCarreraid()));
        periodoExamenRepository.save(periodoExamen);

        actividadService.registrarActividad(TipoActividad.REGISTRO_PERIODO_EXAMEN, "Se ha registrado un nuevo periodo de examen para la carrera con id " + periodoExamenDTO.getCarreraid() + " con fecha de inicio " + fechaInicio + " y fecha de fin " + fechaFin);

        return new PeriodoExamenDTO(periodoExamen);
    }

    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if(fechaInicio.isAfter(fechaFin)) {
            throw new FechaException("La fecha de inicio no puede ser mayor a la fecha de fin");
        }
        if(fechaFin.isBefore(LocalDateTime.now())) {
            throw new FechaException("La fecha de fin no puede ser menor a la fecha actual");
        }
    }

    private void verificarPeriodoExistente(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long carreraId) {
        Optional<PeriodoExamen> existePeriodoExamenCarrera = periodoExamenRepository.findByFechaInicioAndFechaFinAndCarreraId(fechaInicio, fechaFin, carreraId);
        if (existePeriodoExamenCarrera.isPresent()) {
            throw new UniqueFieldException("Ya existe un período de examen con las mismas fechas para la carrera " + carreraId);
        }

        List<PeriodoExamen> periodosExistentes = periodoExamenRepository.findAllByCarreraId(carreraId);
        for (PeriodoExamen periodoExistente : periodosExistentes) {
            if (interseccionDeFechas(fechaInicio, fechaFin, periodoExistente)) {
                throw new FechaException("Las fechas ingresadas están comprendidas dentro de otro periodo existente");
            }
            if (conteniendoFechas(fechaInicio, fechaFin, periodoExistente)) {
                throw new FechaException("Existe otro periodo de examen comprendido en estas fechas.");
            }
        }
    }

    private boolean interseccionDeFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, PeriodoExamen periodoExistente) {
        return (fechaInicio.isAfter(periodoExistente.getFechaInicio()) && fechaInicio.isBefore(periodoExistente.getFechaFin())) ||
                (fechaFin.isAfter(periodoExistente.getFechaInicio()) && fechaFin.isBefore(periodoExistente.getFechaFin())) ||
                fechaInicio.equals(periodoExistente.getFechaInicio()) || fechaFin.equals(periodoExistente.getFechaFin());
    }

    private boolean conteniendoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, PeriodoExamen periodoExistente) {
        return (periodoExistente.getFechaInicio().isAfter(fechaInicio) && periodoExistente.getFechaInicio().isBefore(fechaFin)) ||
                (periodoExistente.getFechaFin().isAfter(fechaInicio) && periodoExistente.getFechaFin().isBefore(fechaFin)) ||
                periodoExistente.getFechaInicio().equals(fechaInicio) || periodoExistente.getFechaFin().equals(fechaFin);
    }

    private Carrera obtenerCarrera(Long carreraId) {
        return carreraRepository.findById(carreraId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada"));
    }

}
