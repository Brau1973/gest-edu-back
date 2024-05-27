package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.repositories.ExamenRepository;
import com.tecnoinf.gestedu.services.implementations.ExamenServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.services.interfaces.ExamenService;
import com.tecnoinf.gestedu.services.interfaces.PeriodoExamenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExamenServiceImplTest {

    @InjectMocks
    private ExamenServiceImpl examenService;

    @Mock
    private PeriodoExamenService periodoExamenService;

    @Mock
    private CarreraService carreraService;

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private ExamenRepository examenRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAltaExamen() {
        CreateExamenDTO createExamenDto = new CreateExamenDTO();
        createExamenDto.setAsignaturaId(1L);
        createExamenDto.setFecha(LocalDateTime.now().plusDays(3));
        createExamenDto.setDiasPrevInsc(5);
        Long[] docenteIds = new Long[1];
        docenteIds[0] = 1L;
        createExamenDto.setDocenteIds(docenteIds);

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        PeriodoExamen periodoExamen = new PeriodoExamen();
        periodoExamen.setId(1L);
        periodoExamen.setCarrera(carrera);
        periodoExamen.setFechaInicio(LocalDateTime.now());
        periodoExamen.setFechaFin(LocalDateTime.now().plusDays(5));

        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCarrera(carrera);

        Docente docente = new Docente();
        docente.setId(1L);

        List<PeriodoExamenDTO> periodosExamen = new ArrayList<>();
        PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO();
        periodoExamenDTO.setId(1L);
        periodoExamenDTO.setFechaInicio(LocalDateTime.now());
        periodoExamenDTO.setFechaFin(LocalDateTime.now().plusDays(5));
        periodosExamen.add(periodoExamenDTO);

        Page<PeriodoExamenDTO> page = new PageImpl<>(periodosExamen);

        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(examenRepository.existsByFechaAndAsignatura(any(LocalDateTime.class), any(Asignatura.class))).thenReturn(false);
        when(carreraService.obtenerPeriodosExamenCarrera(anyLong(), any(Pageable.class))).thenReturn(page);

        examenService.altaExamen(createExamenDto);

        verify(examenRepository, times(1)).save(any(Examen.class));
    }
}