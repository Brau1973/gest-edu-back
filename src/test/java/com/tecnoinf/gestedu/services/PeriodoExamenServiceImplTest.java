package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.exceptions.FechaException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.PeriodoExamen;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.PeriodoExamenRepository;
import com.tecnoinf.gestedu.services.implementations.PeriodoExamenServiceImpl;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PeriodoExamenServiceImplTest {

    @InjectMocks
    private PeriodoExamenServiceImpl periodoExamenService;

    @Mock
    private PeriodoExamenRepository periodoExamenRepository;

    @Mock
    private CarreraRepository carreraRepository;

    @Mock
    private ActividadService actividadService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(actividadService).registrarActividad(any(), any());
    }

    private PeriodoExamenDTO crearPeriodoExamenDTO(LocalDate fechaInicio, LocalDate fechaFin, Long carreraId) {
        PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO();

        periodoExamenDTO.setFechaInicio(fechaInicio);
        periodoExamenDTO.setFechaFin(fechaFin);
        periodoExamenDTO.setCarreraid(carreraId);
        return periodoExamenDTO;
    }

    @Test
    public void testRegistrarPeriodoExamen_FechaInicioMayorAFechaFin() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDate.now().plusDays(1), LocalDate.now(), 1L);
        assertThrows(FechaException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }
//SI ANDA MANUAL
//    @Test
//    public void testRegistrarPeriodoExamen_PeriodoExamenYaExiste() {
//        LocalDateTime fechaInicio = LocalDateTime.now();
//        LocalDateTime fechaFin = LocalDateTime.now().plusDays(1);
//        Long carreraId = 1L;
//
//        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(fechaInicio, fechaFin, carreraId);
//        PeriodoExamen periodoExamenExistente = new PeriodoExamen();
//        periodoExamenExistente.setFechaInicio(fechaInicio);
//        periodoExamenExistente.setFechaFin(fechaFin);
//
//        Carrera carrera = new Carrera();
//        carrera.setId(carreraId);
//
//        // Configurar los mocks
//        when(carreraRepository.findById(carreraId)).thenReturn(Optional.of(carrera));
//        when(periodoExamenRepository.findByFechaInicioAndFechaFinAndCarreraId(fechaInicio, fechaFin, carreraId))
//                .thenReturn(Optional.of(periodoExamenExistente));
//
//        // Ejecutar y verificar la excepción
//        assertThrows(UniqueFieldException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
//    }

    @Test
    public void testRegistrarPeriodoExamen_PeriodoExistenteComprendidoEnNuevo() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDate.now(), LocalDate.now().plusDays(3), 1L);
        PeriodoExamen periodoExamenExistente = new PeriodoExamen();
        periodoExamenExistente.setFechaInicio(LocalDate.now().plusDays(1));
        periodoExamenExistente.setFechaFin(LocalDate.now().plusDays(2));

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid())).thenReturn(Optional.of(carrera));
        when(periodoExamenRepository.findAllByCarreraId(periodoExamenDTO.getCarreraid()))
                .thenReturn(Collections.singletonList(periodoExamenExistente));

        assertThrows(FechaException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

    @Test
    public void testRegistrarPeriodoExamen_NuevoPeriodoComprendidoEnExistente() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        PeriodoExamen periodoExamenExistente = new PeriodoExamen();
        periodoExamenExistente.setFechaInicio(LocalDate.now());
        periodoExamenExistente.setFechaFin(LocalDate.now().plusDays(3));

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid())).thenReturn(Optional.of(carrera));
        when(periodoExamenRepository.findAllByCarreraId(periodoExamenDTO.getCarreraid()))
                .thenReturn(Collections.singletonList(periodoExamenExistente));

        assertThrows(FechaException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

    @Test
    public void testRegistrarPeriodoExamen_CarreraNoExistente() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDate.now(), LocalDate.now().plusDays(1), 1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

    @Test
    public void testRegistrarPeriodoExamen_Exito() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid()))
                .thenReturn(Optional.of(carrera));

        when(periodoExamenRepository.findAllByCarreraId(periodoExamenDTO.getCarreraid()))
                .thenReturn(Collections.emptyList());

        PeriodoExamen periodoExamenGuardado = new PeriodoExamen();
        periodoExamenGuardado.setId(1L);
        periodoExamenGuardado.setFechaInicio(LocalDate.now().plusDays(1));
        periodoExamenGuardado.setFechaFin(LocalDate.now().plusDays(2));
        periodoExamenGuardado.setCarrera(carrera);

        when(periodoExamenRepository.save(new PeriodoExamen())).thenReturn(periodoExamenGuardado);

        assertDoesNotThrow(() -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }
}
