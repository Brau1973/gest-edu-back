package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Tramite;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.TramiteRepository;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TramiteServiceImpl implements TramiteService{

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final TramiteRepository tramiteRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public TramiteServiceImpl(EstudianteRepository estudianteRepository, CarreraRepository carreraRepository, TramiteRepository tramiteRepository, ModelMapper modelMapper, EmailService emailService) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.tramiteRepository = tramiteRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Override
    public TramiteDTO nuevoTramite(Long carreraId, TipoTramite tipoTramite, String email) throws MessagingException {
        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + carreraId));
        Estudiante estudiante = (Estudiante) estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante not found with email " + email));

        Tramite tramite = new Tramite();
        tramite.setCarrera(carrera);
        tramite.setUsuarioSolicitante(estudiante);
        tramite.setEstado(EstadoTramite.PENDIENTE);
        tramite.setTipo(tipoTramite);
        tramite.setUsuarioResponsable(null);

        //TODO cambiar a email del estudiante cuando se pase a produccion
        emailService.sendNuevoTramiteInscripcionCarreraEmail("gestedu.info@gmail.com", estudiante.getNombre(), carrera.getNombre());

        return modelMapper.map(tramiteRepository.save(tramite), TramiteDTO.class);
    }
}
