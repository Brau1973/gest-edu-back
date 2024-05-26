package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.TramitePendienteExistenteException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Tramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.TramiteRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TramiteServiceTest {

    @InjectMocks
    private TramiteServiceImpl tramiteService;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private CarreraRepository carreraRepository;

    @Mock
    private TramiteRepository tramiteRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nuevoTramite_createsNewTramite() throws MessagingException {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setExistePlanEstudio(true);

        Estudiante estudiante = new Estudiante();
        estudiante.setEmail("test@test.com");

        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(estudianteRepository.findByEmail("test@test.com")).thenReturn(Optional.of(estudiante));
        when(tramiteRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(modelMapper.map(any(Tramite.class), eq(TramiteDTO.class))).thenReturn(new TramiteDTO());
        doNothing().when(emailService).sendNuevoTramiteInscripcionCarreraEmail(anyString(), anyString(), anyString());

        TramiteDTO result = tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");

        verify(carreraRepository, times(1)).findById(1L);
        verify(estudianteRepository, times(1)).findByEmail("test@test.com");
        verify(tramiteRepository, times(1)).save(any());
    }

    @Test
    public void nuevoTramite_throwsException_whenCarreraNotFound() {
        when(carreraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");
        });
    }

    @Test
    public void nuevoTramite_throwsException_whenEstudianteNotFound() {
        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(estudianteRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");
        });
    }

    @Test
    public void nuevoTramite_createsNewTramite_whenCarreraHasPlanEstudioAndEstudianteExistsAndNoPendingTramite() throws MessagingException {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setExistePlanEstudio(true);

        Estudiante estudiante = new Estudiante();
        estudiante.setEmail("test@test.com");

        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(estudianteRepository.findByEmail("test@test.com")).thenReturn(Optional.of(estudiante));
        when(tramiteRepository.existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(any(), any(), any(), any())).thenReturn(false);
        when(tramiteRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(modelMapper.map(any(Tramite.class), eq(TramiteDTO.class))).thenReturn(new TramiteDTO());

        TramiteDTO result = tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");

        verify(carreraRepository, times(1)).findById(1L);
        verify(estudianteRepository, times(1)).findByEmail("test@test.com");
        verify(tramiteRepository, times(1)).save(any());
    }

    @Test
    public void nuevoTramite_throwsException_whenCarreraDoesNotHavePlanEstudio() {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setExistePlanEstudio(false);

        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));

        assertThrows(ResourceNotFoundException.class, () -> {
            tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");
        });
    }

    @Test
    public void nuevoTramite_throwsException_whenEstudianteDoesNotExist() {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setExistePlanEstudio(true);

        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(estudianteRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");
        });
    }

    @Test
    public void nuevoTramite_throwsException_whenPendingTramiteExists() {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setExistePlanEstudio(true);

        Estudiante estudiante = new Estudiante();
        estudiante.setEmail("test@test.com");

        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(estudianteRepository.findByEmail("test@test.com")).thenReturn(Optional.of(estudiante));
        when(tramiteRepository.existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(any(), any(), any(), any())).thenReturn(true);

        assertThrows(TramitePendienteExistenteException.class, () -> {
            tramiteService.nuevoTramite(1L, TipoTramite.INSCRIPCION_A_CARRERA, "test@test.com");
        });
    }
}