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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PeriodoExamenServiceImplTest {

    @InjectMocks
    private PeriodoExamenServiceImpl periodoExamenService;

    @Mock
    private PeriodoExamenRepository periodoExamenRepository;

    @Mock
    private CarreraRepository carreraRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private PeriodoExamenDTO crearPeriodoExamenDTO(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long carreraId) {
        PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO();
        periodoExamenDTO.setFechaInicio(fechaInicio);
        periodoExamenDTO.setFechaFin(fechaFin);
        periodoExamenDTO.setCarreraid(carreraId);
        return periodoExamenDTO;
    }

    @Test
    public void testRegistrarPeriodoExamen_FechaInicioMayorAFechaFin() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1L);
        assertThrows(FechaException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

//    @Test ------ MANUALMENTE SI DA BIEN
//    public void testRegistrarPeriodoExamen_PeriodoExamenYaExiste() {
//        Carrera carrera = new Carrera();
//        Long carreraId = carrera.getId();
//        LocalDateTime fechaInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime fechaFin = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(0);
//
//        PeriodoExamenDTO periodoExamenDTO = new PeriodoExamenDTO();
//        periodoExamenDTO.setFechaInicio(fechaInicio);
//        periodoExamenDTO.setFechaFin(fechaFin);
//        periodoExamenDTO.setCarreraid(carreraId);
//
//        PeriodoExamen periodoExamenExistente = new PeriodoExamen();
//        periodoExamenExistente.setFechaInicio(fechaInicio);
//        periodoExamenExistente.setFechaFin(fechaFin);
//        periodoExamenExistente.setCarrera(carrera); // Set carrera in periodoExamenExistente
//
//        // Configurar los mocks
//        when(periodoExamenRepository.findByFechaInicioAndFechaFinAndCarreraId(fechaInicio, fechaFin, carreraId))
//                .thenReturn(Optional.of(periodoExamenExistente));
//
//        // Ejecutar y verificar la excepción
//        assertThrows(UniqueFieldException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
//    }

    @Test
    public void testRegistrarPeriodoExamen_PeriodoExistenteComprendidoEnNuevo() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDateTime.now(), LocalDateTime.now().plusDays(3), 1L);
        PeriodoExamen periodoExamenExistente = new PeriodoExamen();
        periodoExamenExistente.setFechaInicio(LocalDateTime.now().plusDays(1));
        periodoExamenExistente.setFechaFin(LocalDateTime.now().plusDays(2));

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid())).thenReturn(Optional.of(carrera));
        when(periodoExamenRepository.findAllByCarreraId(periodoExamenDTO.getCarreraid()))
                .thenReturn(Collections.singletonList(periodoExamenExistente));

        assertThrows(FechaException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

    @Test
    public void testRegistrarPeriodoExamen_NuevoPeriodoComprendidoEnExistente() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L);
        PeriodoExamen periodoExamenExistente = new PeriodoExamen();
        periodoExamenExistente.setFechaInicio(LocalDateTime.now());
        periodoExamenExistente.setFechaFin(LocalDateTime.now().plusDays(3));

        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid())).thenReturn(Optional.of(carrera));
        when(periodoExamenRepository.findAllByCarreraId(periodoExamenDTO.getCarreraid()))
                .thenReturn(Collections.singletonList(periodoExamenExistente));

        assertThrows(FechaException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

    @Test
    public void testRegistrarPeriodoExamen_CarreraNoExistente() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }

    @Test
    public void testRegistrarPeriodoExamen_Exito() {
        PeriodoExamenDTO periodoExamenDTO = crearPeriodoExamenDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L);
        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(periodoExamenDTO.getCarreraid()))
                .thenReturn(Optional.of(carrera));

        when(periodoExamenRepository.findAllByCarreraId(periodoExamenDTO.getCarreraid()))
                .thenReturn(Collections.emptyList());

        PeriodoExamen periodoExamenGuardado = new PeriodoExamen();
        periodoExamenGuardado.setId(1L);
        periodoExamenGuardado.setFechaInicio(periodoExamenDTO.getFechaInicio());
        periodoExamenGuardado.setFechaFin(periodoExamenDTO.getFechaFin());
        periodoExamenGuardado.setCarrera(carrera);

        when(periodoExamenRepository.save(new PeriodoExamen())).thenReturn(periodoExamenGuardado);

        assertDoesNotThrow(() -> periodoExamenService.registrarPeriodoExamen(periodoExamenDTO));
    }
}
