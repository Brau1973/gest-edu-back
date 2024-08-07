package com.tecnoinf.gestedu.services.implementations;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.TramiteNotFoundException;
import com.tecnoinf.gestedu.exceptions.TramiteNotPendienteException;
import com.tecnoinf.gestedu.exceptions.TramitePendienteExistenteException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.*;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EstudianteService estudianteService;
    private final NotificacionService notificacionService;
    private final NotificacionRepository notificacionRepository;

    @Autowired
    public TramiteServiceImpl(EstudianteRepository estudianteRepository, CarreraRepository carreraRepository,
                              TramiteRepository tramiteRepository, ModelMapper modelMapper, EmailService emailService,
                              UsuarioRepository usuarioRepository, InscripcionCarreraService inscripcionCarreraService,
                              TituloService tituloService, ActividadService actividadService, EstudianteService estudianteService,
                              NotificacionService notificacionService, NotificacionRepository notificacionRepository) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.tramiteRepository = tramiteRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
        this.inscripcionCarreraService = inscripcionCarreraService;
        this.tituloService = tituloService;
        this.actividadService = actividadService;
        this.estudianteService = estudianteService;
        this.notificacionService = notificacionService;
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public TramiteDTO nuevoTramite(Long carreraId, TipoTramite tipoTramite, String email) throws MessagingException {
        Carrera carrera = checkCarreraExistsYTienePlanEstudio(carreraId);
        Estudiante estudiante = getEstudianteByEmail(email);

        //Verificar si el estudiante ya tiene un trámite pendiente asociado con la misma carrera y con el mismo tipo
        if (tramiteRepository.existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(estudiante, carrera, tipoTramite, EstadoTramite.PENDIENTE)) {
            throw new TramitePendienteExistenteException("El estudiante " + estudiante.getNombre() + " ya tiene un trámite pendiente del tipo "
                    + tipoTramite + " relacionado con la carrera " +carrera.getNombre());
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
                .map(this::mapTramiteToDTOAndAddCreditosAprobados)
                .collect(Collectors.toList());
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
                .map(this::mapTramiteToDTOAndAddCreditosAprobados)
                .collect(Collectors.toList());
    }

    @Override
    public List<TramiteDTO> listarTramitesInscripcionCarreraResueltos() {
        List<Tramite> tramites = tramiteRepository.findAllByTipoAndEstadoIn(TipoTramite.INSCRIPCION_A_CARRERA, List.of(EstadoTramite.ACEPTADO, EstadoTramite.RECHAZADO));
        return tramites.stream()
                .map(tramite -> modelMapper.map(tramite, TramiteDTO.class))
                .toList();
    }

    @Override
    public List<TramiteDTO> listarTramitesSolicitudTituloResueltos() {
        List<Tramite> tramites = tramiteRepository.findAllByTipoAndEstadoIn(TipoTramite.SOLICITUD_DE_TITULO, List.of(EstadoTramite.ACEPTADO, EstadoTramite.RECHAZADO));
        return tramites.stream()
                .map(this::mapTramiteToDTOAndAddCreditosAprobados)
                .collect(Collectors.toList());
    }

    @Override
    public TramiteDTO getTramiteById(Long tramiteId) {
        Tramite tramite = obtenerTramiteById(tramiteId);
        return this.mapTramiteToDTOAndAddCreditosAprobados(tramite);
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
        Tramite tramite = obtenerTramiteById(tramiteId);
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
                actividadService.registrarActividad(TipoActividad.APROBACION_SOLICITUD_INSCRIPCION_CARRERA, "Se aprobo la solicitud de inscripcion del estudiante " + estudianteSolicitante.getNombre() + " " + estudianteSolicitante.getApellido() + " a la carrera " + carrera.getNombre());
                emailService.sendAprobacionTramiteInscripcionCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre());
            } else if (estadoTramite == EstadoTramite.RECHAZADO){
                actividadService.registrarActividad(TipoActividad.RECHAZO_SOLICITUD_INSCRIPCION_CARRERA, "Se rechazo la solicitud de inscripcion del estudiante " + estudianteSolicitante.getNombre() + " " + estudianteSolicitante.getApellido() + " a la carrera " + carrera.getNombre());
                emailService.sendRechazoTramiteInscripcionCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre(), motivoRechazo);
            }
        } else if (tipoTramite == TipoTramite.SOLICITUD_DE_TITULO){
            if (estadoTramite == EstadoTramite.ACEPTADO){
                tituloService.createTitulo(carrera.getNombre(),estudianteSolicitante);
                actividadService.registrarActividad(TipoActividad.APROBACION_SOLICITUD_TITULO, "Se aprobo la solicitud de titulo del estudiante " + estudianteSolicitante.getNombre() + " " + estudianteSolicitante.getApellido() + " de la carrera " + carrera.getNombre());
                emailService.sendAprobacionTramiteTituloCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre());
            } else if (estadoTramite == EstadoTramite.RECHAZADO){
                actividadService.registrarActividad(TipoActividad.RECHAZO_SOLICITUD_TITULO, "Se rechazo la solicitud de titulo del estudiante " + estudianteSolicitante.getNombre() + " " + estudianteSolicitante.getApellido() + " de la carrera " + carrera.getNombre());
                emailService.sendRechazoTramiteTituloCarreraEmail("gestedu.info@gmail.com", estudianteSolicitante.getNombre(), carrera.getNombre(), usuarioResponsable.getNombre(), motivoRechazo);
            }
        }
        enviarNotificaciones(carrera, estudianteSolicitante, usuarioResponsable.getNombre(), estadoTramite, motivoRechazo, tipoTramite);

        return modelMapper.map(savedTramite, TramiteDTO.class);
    }

    private void enviarNotificaciones(Carrera carrera, Estudiante estudianteSolicitante, String usuarioResponsable, EstadoTramite estadoTramite, String motivoRechazo, TipoTramite tipoTramite){
        Notificacion notificacion = new Notificacion(LocalDate.now(), false, estudianteSolicitante);
        notificacion.setTitulo("Tramite " + tipoTramite + " " + estadoTramite);
        if(estadoTramite == EstadoTramite.RECHAZADO){
            notificacion.setDescripcion("El tramite " + tipoTramite + " de la carrera " + carrera.getNombre() + " ha sido " + estadoTramite + " por " + usuarioResponsable + " por el motivo: " + motivoRechazo);
        } else{
            notificacion.setDescripcion("El tramite " + tipoTramite + " de la carrera " + carrera.getNombre() + " ha sido " + estadoTramite + " por " + usuarioResponsable + ".");
        }
        notificacionRepository.save(notificacion);

        try {
            List<String> tokens = estudianteSolicitante.getTokenFirebase();
            if (tokens == null || tokens.isEmpty()) {
                System.err.println("No se encontraron tokens para el estudiante: " + estudianteSolicitante.getNombre());
            } else {
                notificacionService.enviarNotificacion(notificacion, tokens);
            }
        } catch (FirebaseMessagingException e) {
            System.err.println("Error al enviar notificación: " + e.getMessage());
        }
    }

    private TramiteDTO mapTramiteToDTOAndAddCreditosAprobados(Tramite tramite) {
        TramiteDTO tramiteDTO = modelMapper.map(tramite, TramiteDTO.class);
        Estudiante estudianteSolicitante = getEstudianteSolicitanteByEmail(tramite);
        Integer creditosAprobados = estudianteService.obtenerCreditosAprobados(estudianteSolicitante, tramite.getCarrera());
        tramiteDTO.setCreditosAprobados(creditosAprobados);
        return tramiteDTO;
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

    private Tramite obtenerTramiteById(Long tramiteId) {
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