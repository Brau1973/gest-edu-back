package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.TramiteNotFoundException;
import com.tecnoinf.gestedu.exceptions.TramiteNotPendienteException;
import com.tecnoinf.gestedu.exceptions.TramitePendienteExistenteException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.TramiteRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.*;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TramiteServiceImpl implements TramiteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final TramiteRepository tramiteRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final InscripcionCarreraService inscripcionCarreraService;
    private final TituloService tituloService;
    private final ActividadService actividadService;

    @Autowired
    public TramiteServiceImpl(EstudianteRepository estudianteRepository, CarreraRepository carreraRepository,
                              TramiteRepository tramiteRepository, ModelMapper modelMapper, EmailService emailService,
                              UsuarioRepository usuarioRepository, InscripcionCarreraService inscripcionCarreraService,
                              TituloService tituloService, ActividadService actividadService) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.tramiteRepository = tramiteRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
        this.inscripcionCarreraService = inscripcionCarreraService;
        this.tituloService = tituloService;
        this.actividadService = actividadService;
    }

    @Override
    public TramiteDTO nuevoTramite(Long carreraId, TipoTramite tipoTramite, String email) throws MessagingException {
        Carrera carrera = checkCarreraExistsYTienePlanEstudio(carreraId);
        Estudiante estudiante = getEstudianteByEmail(email);

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

        if(tipoTramite == TipoTramite.INSCRIPCION_A_CARRERA){
            //TODO cambiar a email del estudiante cuando se pase a produccion
            emailService.sendNuevoTramiteInscripcionCarreraEmail("gestedu.info@gmail.com", estudiante.getNombre(), carrera.getNombre());
            actividadService.registrarActividad(TipoActividad.SOLICITUD_INSCRIPCION_CARRERA, "Se solicito la inscripcion a la carrera " + carrera.getNombre());
        } else if (tipoTramite == TipoTramite.SOLICITUD_DE_TITULO){
            //TODO cambiar a email del estudiante cuando se pase a produccion
            actividadService.registrarActividad(TipoActividad.SOLICITUD_TITULO, "Se solicito el titulo de la carrera " + carrera.getNombre());
            emailService.sendNuevoTramiteTituloCarreraEmail("gestedu.info@gmail.com", estudiante.getNombre(), carrera.getNombre());
        }
        return modelMapper.map(tramiteRepository.save(tramite), TramiteDTO.class);
    }

    @Override
    public List<TramiteDTO> listarTramitesInscripcionCarreraPendientes() {
        List<Tramite> tramites = tramiteRepository.findAllByTipoAndEstado(TipoTramite.INSCRIPCION_A_CARRERA, EstadoTramite.PENDIENTE);
        return tramites.stream()
                .map(tramite -> modelMapper.map(tramite, TramiteDTO.class))
                .toList();
    }

    @Override
    public List<TramiteDTO> listarTramitesSolicitudTituloPendientes() {
        List<Tramite> tramites = tramiteRepository.findAllByTipoAndEstado(TipoTramite.SOLICITUD_DE_TITULO, EstadoTramite.PENDIENTE);
        return tramites.stream()
                .map(tramite -> modelMapper.map(tramite, TramiteDTO.class))
                .toList();
    }

    @Override
    public TramiteDTO aprobarTramiteSolicitudTitulo(Long tramiteId, String email) throws MessagingException {
        return processTramite(tramiteId, email, EstadoTramite.ACEPTADO, null);
    }

    @Override
    public TramiteDTO rechazarTramiteSolicitudTitulo(Long tramiteId, String email, String motivoRechazo) throws MessagingException {
        return processTramite(tramiteId, email, EstadoTramite.RECHAZADO, motivoRechazo);
    }

    @Override
    public List<TramiteDTO> listarTramitesEstudiante(String email) {
        Estudiante estudiante = getEstudianteByEmail(email);
        List<Tramite> tramites = tramiteRepository.findAllByUsuarioSolicitante(estudiante);
        return tramites.stream()
                .map(tramite -> modelMapper.map(tramite, TramiteDTO.class))
                .toList();
    }

    @Override
    public TramiteDTO aprobarTramiteInscripcionCarrera(Long tramiteId, String email) throws MessagingException {
        return processTramite(tramiteId, email, EstadoTramite.ACEPTADO, null);
    }

    @Override
    public TramiteDTO rechazarTramiteInscripcionCarrera(Long tramiteId, String email, String motivoRechazo) throws MessagingException {
        return processTramite(tramiteId, email, EstadoTramite.RECHAZADO, motivoRechazo);
    }

    private TramiteDTO processTramite(Long tramiteId, String email, EstadoTramite estado, String motivoRechazo) throws MessagingException {
        Tramite tramite = getTramiteById(tramiteId);
        verificarEstadoPendienteTramite(tramite);
        Usuario usuarioResponsable = getUsuarioResponsableByEmail(email);

        tramite.setEstado(estado);
        tramite.setFechaActualizacion(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        if(estado == EstadoTramite.RECHAZADO){
            tramite.setMotivoRechazo(motivoRechazo);
        }
        tramite.setUsuarioResponsable(usuarioResponsable);
        Tramite savedTramite = tramiteRepository.save(tramite);

        TipoTramite tipoTramite = savedTramite.getTipo();
        EstadoTramite estadoTramite = savedTramite.getEstado();
        Estudiante estudianteSolicitante =  getEstudianteSolicitanteByEmail(tramite);
        Carrera carrera = tramite.getCarrera();

        //TODO cambiar a email de las llamadas a emailService por el  usuarioEstudiante cuando se pase a produccion
        if(tipoTramite == TipoTramite.INSCRIPCION_A_CARRERA){
            if (estadoTramite == EstadoTramite.ACEPTADO){
                inscripcionCarreraService.createInscripcionCarrera(carrera, estudianteSolicitante);
                actividadService.registrarActividad(TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudianteSolicitante.getNombre() + " a la carrera " + carrera.getNombre());
                emailService.sendAprobacionTramiteInscripcionCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre());
            } else if (estadoTramite == EstadoTramite.RECHAZADO){
                actividadService.registrarActividad(TipoActividad.RECHAZO_SOLICITUD_INSCRIPCION_CARRERA, "Se rechazo la solicitud de inscripcion del estudiante " + estudianteSolicitante.getNombre() + " a la carrera " + carrera.getNombre());
                emailService.sendRechazoTramiteInscripcionCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre(), motivoRechazo);
            }
        } else if (tipoTramite == TipoTramite.SOLICITUD_DE_TITULO){
            if (estadoTramite == EstadoTramite.ACEPTADO){
                tituloService.createTitulo(carrera.getNombre(),estudianteSolicitante);
                actividadService.registrarActividad(TipoActividad.APROBACION_SOLICITUD_TITULO, "Se aprobo la solicitud de titulo del estudiante " + estudianteSolicitante.getNombre() + " de la carrera " + carrera.getNombre());
                emailService.sendAprobacionTramiteTituloCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre());
            } else if (estadoTramite == EstadoTramite.RECHAZADO){
                actividadService.registrarActividad(TipoActividad.RECHAZO_SOLICITUD_TITULO, "Se rechazo la solicitud de titulo del estudiante " + estudianteSolicitante.getNombre() + " de la carrera " + carrera.getNombre());
                emailService.sendRechazoTramiteTituloCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre(), motivoRechazo);
            }
        }

        return modelMapper.map(savedTramite, TramiteDTO.class);
    }

    private Estudiante getEstudianteByEmail(String email) {
        return (Estudiante) estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante not found with email " + email));
    }

    private Carrera checkCarreraExistsYTienePlanEstudio(Long carreraId) {
        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + carreraId));

        if (!carrera.getExistePlanEstudio()) {
            throw new ResourceNotFoundException("No se puede inscribir a la carrera " + carrera.getNombre() + " porque no tiene un plan de estudio asociado");
        }

        return carrera;
    }

    private Tramite getTramiteById(Long tramiteId) {
        return tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new TramiteNotFoundException("Tramite not found with id " + tramiteId));
    }

    private void verificarEstadoPendienteTramite(Tramite tramite) {
        if (!tramite.getEstado().equals(EstadoTramite.PENDIENTE)) {
            throw new TramiteNotPendienteException("Tramite is not in PENDIENTE state");
        }
    }

    private Estudiante getEstudianteSolicitanteByEmail(Tramite tramite) {
        String email =  tramite.getUsuarioSolicitante().getEmail();
        return estudianteRepository.findEstudianteByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante not found with email " + email));
    }

    private Usuario getUsuarioResponsableByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with email " + email));
    }

}
