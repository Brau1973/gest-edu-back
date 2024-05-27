package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.exceptions.FechaException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.Examen;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.repositories.ExamenRepository;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.services.interfaces.DocenteService;
import com.tecnoinf.gestedu.services.interfaces.ExamenService;
import com.tecnoinf.gestedu.services.interfaces.PeriodoExamenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamenServiceImpl implements ExamenService {

    private static final int DIAS_PREV_INSC_DEFAULT = 10;

    private final PeriodoExamenService periodoExamenService;
    private final CarreraService carreraService;
    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final ExamenRepository examenRepository;

    public ExamenServiceImpl(PeriodoExamenService periodoExamenService, CarreraService carreraService, AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository, ExamenRepository examenRepository){
        this.periodoExamenService = periodoExamenService;
        this.carreraService = carreraService;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.examenRepository = examenRepository;
    }

    @Override
    public ExamenDTO altaExamen(CreateExamenDTO createExamenDto) {
        Asignatura asignatura = obtenerAsignatura(createExamenDto.getAsignaturaId());

        List<Docente> docentes = new ArrayList<>();
        for (Long docenteId : createExamenDto.getDocenteIds()) {
            Docente docente = docenteRepository.findById(docenteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Docente con id " + docenteId + " no encontrado"));
            docentes.add(docente);
        }
        if (docentes.isEmpty()) {
            throw new ResourceNotFoundException("Se requiere al menos un docente para crear un examen.");
        }

        validarFechaExamen(createExamenDto.getFecha(), asignatura);

        Examen examen = crearExamen(createExamenDto, asignatura, docentes);
        examenRepository.save(examen);
        return new ExamenDTO(examen);
    }

    private Asignatura obtenerAsignatura(Long asignaturaId) {
        if (asignaturaId == null) {
            throw new ResourceNotFoundException("Se requiere una asignatura para crear un examen.");
        }
        return asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada."));
    }

    private void validarFechaExamen(LocalDateTime fechaExamen, Asignatura asignatura) {
        LocalDateTime fecha = fechaExamen.withSecond(0).withNano(0);
        if (examenRepository.existsByFechaAndAsignatura(fecha, asignatura)) {
            throw new UniqueFieldException("Ya existe un examen programado para esta asignatura en la misma fecha y hora.");
        }
        List<PeriodoExamenDTO> periodosExamen = carreraService.obtenerPeriodosExamenCarrera(asignatura.getCarrera().getId(), Pageable.unpaged()).getContent();
        if (!isFechaDentroDePeriodo(fecha, periodosExamen)) {
            throw new FechaException("La fecha del examen no está dentro de un periodo de examen vigente.");
        }
    }

    private Examen crearExamen(CreateExamenDTO createExamenDto, Asignatura asignatura, List<Docente> docentes) {
        Examen examen = new Examen();
        examen.setFecha(createExamenDto.getFecha().withSecond(0).withNano(0));
        examen.setDiasPrevInsc(createExamenDto.getDiasPrevInsc() != null ? createExamenDto.getDiasPrevInsc() : DIAS_PREV_INSC_DEFAULT);
        examen.setAsignatura(asignatura);
        examen.setDocentes(docentes);
        return examen;
    }

    private boolean isFechaDentroDePeriodo(LocalDateTime fechaExamen, List<PeriodoExamenDTO> periodosExamen) {
        return periodosExamen.stream().anyMatch(periodoExamen ->
                (fechaExamen.isAfter(periodoExamen.getFechaInicio()) || fechaExamen.isEqual(periodoExamen.getFechaInicio())) &&
                        (fechaExamen.isBefore(periodoExamen.getFechaFin()) || fechaExamen.isEqual(periodoExamen.getFechaFin())));
    }
}
