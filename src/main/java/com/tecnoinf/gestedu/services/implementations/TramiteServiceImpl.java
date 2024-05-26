package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.TramitePendienteExistenteException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Tramite;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.TramiteRepository;
import com.tecnoinf.gestedu.services.interfaces.EmailService;
import com.tecnoinf.gestedu.services.interfaces.TramiteService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TramiteServiceImpl implements TramiteService {

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

        if (!carrera.getExistePlanEstudio()) {
            throw new ResourceNotFoundException("No se puede inscribir a la carrera " + carrera.getNombre() + " porque no tiene un plan de estudio asociado");
        }

        Estudiante estudiante = (Estudiante) estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante not found with email " + email));

        //Verificar si el estudiante ya tiene un trámite pendiente asociado con la misma carrera y con el mismo tipo
        if (tramiteRepository.existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(estudiante, carrera, tipoTramite, EstadoTramite.PENDIENTE)) {
            throw new TramitePendienteExistenteException("El estudiante " + estudiante.getNombre() + " ya tiene un trámite pendiente de inscripción a la carrera " + carrera.getNombre());
        }

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

    @Override
    public List<TramiteDTO> listarTramitesInscripcionCarreraPendientes() {
        List<Tramite> tramites = tramiteRepository.findAllByTipoAndEstado(TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE);
        return tramites.stream()
                .map(tramite -> modelMapper.map(tramite, TramiteDTO.class))
                .toList();
    }
}
