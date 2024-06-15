package com.tecnoinf.gestedu.services.implementations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.NotificacionService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoHorarioDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.exceptions.CalificacionCursoException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCurso;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.EmailService;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCursoService;

@Service
public class InscripcionCursoServiceImpl implements InscripcionCursoService {
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final InscripcionCarreraRepository inscripcionCarreraRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final ActividadService actividadService;
    private final NotificacionRepository notificacionRepository;
    private final NotificacionService notificacionService;


    @Autowired
    public InscripcionCursoServiceImpl(CursoRepository cursoRepository, EstudianteRepository estudianteRepository,
                                       InscripcionCursoRepository inscripcionCursoRepository, InscripcionCarreraRepository inscripcionCarreraRepository,
                                       AsignaturaRepository asignaturaRepository, UsuarioRepository usuarioRepository, EmailService emailService, ModelMapper modelMapper,
                                       ActividadService actividadService, NotificacionRepository notificacionRepository, NotificacionService notificacionService) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.actividadService = actividadService;
        this.notificacionRepository = notificacionRepository;
        this.notificacionService = notificacionService;
    }

    InscripcionCarrera estaInscriptoEnCarrera(List<InscripcionCarrera> inscripcionCarrera, Curso curso) {
        for (InscripcionCarrera auxInscripcionCarrera : inscripcionCarrera) {
            List<Asignatura> asignaturas = auxInscripcionCarrera.getCarrera().getAsignaturas();
            for (Asignatura auxAsignaturas : asignaturas) {
                if (auxAsignaturas.getId().equals(curso.getAsignatura().getId())) {
                    return auxInscripcionCarrera;
                }
            }
        }
        return null;
    }

    Boolean exoneroCursosPrevios(List<Asignatura> asignaturasPrevias, List<InscripcionCurso> inscripcionCursos) {
        Integer cantPreviasExoneradas = 0;
        Integer contPrevias = 0;
        for (Asignatura auxAsignatura : asignaturasPrevias) {
            contPrevias++;
            for (InscripcionCurso auxInscripcionCurso : inscripcionCursos) {
                //Si la asignatura es previa a un curso cursado
                if (auxAsignatura.getId().equals(auxInscripcionCurso.getCurso().getAsignatura().getId())) {
                    //Chequeo que el alumno pasó el curso
                    if (auxInscripcionCurso.getCalificacion().equals(CalificacionCurso.EXONERADO)) {
                        cantPreviasExoneradas++;
                    }
                }
            }
        }
        if (cantPreviasExoneradas.equals(contPrevias)) {
            return true;
        } else {
            return false;
        }
    }

    Boolean yaInscripto(List<InscripcionCurso> inscripcionCursos, Long cursoId){
        for(InscripcionCurso auxInscCurso: inscripcionCursos){
            if(auxInscCurso.getCurso().getId().equals(cursoId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public InscripcionCursoDTO createInscripcionCurso(InscripcionCursoDTO inscripcionCursoDTO, String name) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(name);
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        Estudiante estudiante = (Estudiante) usuario.get();

        Curso curso = cursoRepository.findById(inscripcionCursoDTO.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + inscripcionCursoDTO.getCursoId()));

        //Me fijo en qué carreras está inscripto el estudiante
        List<InscripcionCarrera> inscripcionTodasCarrerasAlumno = inscripcionCarreraRepository.findInscripcionCarreraEstudianteById(inscripcionCursoDTO.getEstudianteId());
        if (inscripcionTodasCarrerasAlumno != null) {
            //Selecciono la carrera perteneciente al curso
            InscripcionCarrera inscripcionCarrera = estaInscriptoEnCarrera(inscripcionTodasCarrerasAlumno, curso);
            //Si esta inscripto en la carrera.
            if (inscripcionCarrera != null) {
                //Si está cursando la carrera.
                if (inscripcionCarrera.getEstado().toString().equals(EstadoInscripcionCarrera.CURSANDO.toString())) {
                    //Reviso las previas que tiene la asignatura.
                    List<Asignatura> asignaturasPrevias = asignaturaRepository.findPreviasByAsignaturaId(curso.getAsignatura().getId());
                    //Para eso... Listo las inscripciones a los cursos de los estudiantes
                    List<InscripcionCurso> inscripcionCursos = inscripcionCursoRepository.findInscripcionCursoEstudianteById(estudiante.getId());
                    if(!yaInscripto(inscripcionCursos, curso.getId())){
                        //Si no tiene previas...
                        if (asignaturasPrevias == null) {
                            //Crea la inscripción.
                            InscripcionCurso inscripcionCurso = new InscripcionCurso();
                            inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
                            inscripcionCurso.setEstado(EstadoInscripcionCurso.CURSANDO);
                            inscripcionCurso.setEstudiante(estudiante);
                            inscripcionCurso.setCurso(curso);
                            InscripcionCurso createdInscripcion = inscripcionCursoRepository.save(inscripcionCurso);
                            return modelMapper.map(createdInscripcion, InscripcionCursoDTO.class);
                        }
                        //Si tiene previas...
                        else {
                            //Tengo que chequear que las haya pasado
                            if (exoneroCursosPrevios(asignaturasPrevias, inscripcionCursos)) {
                                InscripcionCurso inscripcionCurso = new InscripcionCurso();
                                inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
                                inscripcionCurso.setEstudiante(estudiante);
                                inscripcionCurso.setCurso(curso);
                                inscripcionCurso.setFechaInscripcion(LocalDate.now());
                                InscripcionCurso createdInscripcion = inscripcionCursoRepository.save(inscripcionCurso);

                                actividadService.registrarActividad(TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso con id " + inscripcionCursoDTO.getCursoId() + " exitosa.");

                                return modelMapper.map(createdInscripcion, InscripcionCursoDTO.class);
                            } else {
                                throw new IllegalArgumentException("Faltan exonerar asignaturas previamente.");
                            }
                        }
                    }
                    else {
                        throw new IllegalArgumentException("Ya se encuentra inscripto en el Curso.");
                    }

                } else {
                    throw new IllegalArgumentException("Carrera Completada.");
                }
            }
            else {
                throw new IllegalArgumentException("El Estudiante no esta inscripto en la carrera.");
            }
        } else {
            throw new IllegalArgumentException("El curso no corresponde con su carrera.");
        }
    }

    @Override
    public List<InscripcionCursoCalificacionDTO> registrarCalificaciones(Long id, List<InscripcionCursoCalificacionDTO> calificaciones) throws MessagingException {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));
        if (calificaciones.isEmpty()) {
            throw new CalificacionCursoException("No se han enviado calificaciones para registrar.");
        }
        if(curso.getEstado().equals(Estado.FINALIZADO)){
            throw new CalificacionCursoException("El curso ya tiene calificaciones.");
        }
        if(curso.getInscripciones().size() != calificaciones.size()){
            throw new CalificacionCursoException("No todos los estudiantes fueron calificados");
        }

        for (InscripcionCursoCalificacionDTO calificacionDTO: calificaciones){
            Usuario estudiante = estudianteRepository.findById(calificacionDTO.getEstudianteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));;
            if(calificacionDTO.getCalificacionCurso() == null){
                throw new CalificacionCursoException("La calificación no puede ser nula.");
            }
            if(calificacionDTO.getCalificacionCurso().equals(CalificacionCurso.PENDIENTE)){
                throw new CalificacionCursoException("No pueden quedar calificaciones pendientes.");
            }

            InscripcionCurso inscripcionCurso = inscripcionCursoRepository.findInscripcionCursoEstudianteByEstudianteIdAndCursoId(calificacionDTO.getEstudianteId(), id)
                    .orElseThrow(() -> new ResourceNotFoundException("Inscripción a curso no encontrada."));

            inscripcionCurso.setCalificacion(calificacionDTO.getCalificacionCurso());
            inscripcionCurso.setEstado(EstadoInscripcionCurso.COMPLETADA);
            inscripcionCursoRepository.save(inscripcionCurso);

            actividadService.registrarActividad(TipoActividad.REGISTRO_CALIFICACION, "Registro de calificaciónes de curso");

            enviarNotificaciones(inscripcionCurso);
        }

        curso.setEstado(Estado.FINALIZADO);
        cursoRepository.save(curso);

        return curso.getInscripciones()
                .stream()
                .map(InscripcionCursoCalificacionDTO::new)
                .toList();
    }

    private void enviarNotificaciones(InscripcionCurso inscripcionCurso) throws MessagingException {
        Notificacion notificacion = new Notificacion(LocalDate.now(), false, inscripcionCurso.getEstudiante());
        notificacion.setTitulo("Calificación de curso");
        notificacion.setDescripcion("Se ha registrado la calificación del curso " + inscripcionCurso.getCurso().getAsignatura().getNombre());
        notificacionRepository.save(notificacion);

        try {
            List<String> tokens = inscripcionCurso.getEstudiante().getTokenFirebase();
            if (tokens == null || tokens.isEmpty()) {
                System.err.println("No se encontraron tokens para el estudiante: " + inscripcionCurso.getEstudiante().getNombre());
            } else {
                notificacionService.enviarNotificacion(notificacion, tokens);
            }
        } catch (FirebaseMessagingException e) {
            System.err.println("Error al enviar notificación: " + e.getMessage());
        }
        // Enviar el correo electrónico
        emailService.sendCalificacionCursoEmail(inscripcionCurso.getEstudiante().getEmail(),
                inscripcionCurso.getEstudiante().getNombre(), inscripcionCurso.getCurso().getAsignatura().getCarrera().getNombre(),
                inscripcionCurso.getCurso().getAsignatura().getNombre(), inscripcionCurso.getCalificacion().toString());
    }

    @Override
    public void deleteInscripcionCurso(Long inscripcionCursoId){
        InscripcionCurso inscripcionCurso = inscripcionCursoRepository.findById(inscripcionCursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada."));

        Curso curso = inscripcionCurso.getCurso();

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicioCurso = curso.getFechaInicio();
        int plazoDiasPrevios = curso.getDiasPrevInsc();

        LocalDate limiteFechaInicio = fechaInicioCurso.minusDays(plazoDiasPrevios);

        if (fechaActual.isBefore(fechaInicioCurso)) {
            if (fechaActual.isAfter(limiteFechaInicio)) {
                inscripcionCursoRepository.deleteById(inscripcionCursoId);
            }
            else {
                throw new IllegalStateException("Aún no comienza el plazo de inscripción.");
            }
        }
        else {
            throw new IllegalStateException("No se puede eliminar la inscripción al curso cuando el curso ya ha comenzado.");
        }
    }

    @Override
    public List<CursoHorarioDTO> listarCursosHorariosInscriptos(String name){
        Optional<Usuario> usuario = usuarioRepository.findByEmail(name);
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        Estudiante estudiante = (Estudiante) usuario.get();
        //La lista que voy a devolver al final
        List<CursoHorarioDTO> cursoHorarioDTOS = new ArrayList<>();
        //Busco todas las inscripciones de un alumno
        List<InscripcionCurso> inscripcionCursos = inscripcionCursoRepository.findInscripcionCursoEstudianteById(estudiante.getId());
        //Si no es null...
        if(inscripcionCursos != null){
            //Recorro cada una de las inscripciones
            for(InscripcionCurso inscripciones: inscripcionCursos){
                //un CursoHorario para agregar a la lista a devolver
                CursoHorarioDTO auxCursoHorario = new CursoHorarioDTO();
                //Buscamos el curso
                Optional<Curso> cursoOptional = cursoRepository.findById(inscripciones.getCurso().getId());
                Curso curso = cursoOptional.orElseThrow(() -> new RuntimeException("Curso no encontrado"));
                //Ponemos los datos
                auxCursoHorario.setCursoId(curso.getId());
                auxCursoHorario.setFechaInicio(curso.getFechaInicio());
                auxCursoHorario.setFechaFin(curso.getFechaFin());
                auxCursoHorario.setEstado(curso.getEstado());
                auxCursoHorario.setDocenteNombre(curso.getDocente().getNombre());
                auxCursoHorario.setDocenteApellido(curso.getDocente().getApellido());
                auxCursoHorario.setAsignaturaNombre(curso.getAsignatura().getNombre());
                //auxCursoHorario.setDiasPrevInsc(curso.getDiasPrevInsc());

                List<HorarioDTO> dtoHorarios = new ArrayList<>();
                List<Horario> horarios = curso.getHorarios();
                for(Horario auxHorarios: horarios){
                    HorarioDTO auxDtoHorario = new HorarioDTO();
                    auxDtoHorario.setDia(auxHorarios.getDia());
                    auxDtoHorario.setHoraInicio(auxHorarios.getHoraInicio());
                    auxDtoHorario.setHoraFin(auxHorarios.getHoraFin());
                    dtoHorarios.add(auxDtoHorario);
                }
                auxCursoHorario.setHorarios(dtoHorarios);

                cursoHorarioDTOS.add(auxCursoHorario);
            }
        }
        else{
            throw new IllegalStateException("Inscripciones no encontradas.");
        }
        return cursoHorarioDTOS;
    }

    @Override
    public InscripcionCursoDTO darseDeBajaCurso(Long cursoId, String name){
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));
        LocalDate hoy = LocalDate.now();
        LocalDate finalFechaInscripcion = curso.getFechaInicio();
        LocalDate inicioFechaInscripcion = curso.getFechaInicio().minusDays(curso.getDiasPrevInsc());
        if(hoy.isBefore(finalFechaInscripcion)){
            if(hoy.isAfter(inicioFechaInscripcion)){
                Optional<Usuario> usuario = usuarioRepository.findByEmail(name);
                if (usuario.isEmpty()) {
                    throw new ResourceNotFoundException("Usuario no encontrado.");
                }
                Estudiante estudiante = (Estudiante) usuario.get();
                InscripcionCurso inscripcion = inscripcionCursoRepository.findInscripcionCursoEstudianteByEstudianteIdAndCursoId(estudiante.getId(), cursoId)
                        .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada."));
                inscripcionCursoRepository.delete(inscripcion);

                actividadService.registrarActividad(TipoActividad.BAJA_DE_CURSO, "Baja de curso con id " + cursoId);

                return new InscripcionCursoDTO(inscripcion);
            }
            else {
                throw new IllegalStateException("Aún no está disponible la inscripción.");
            }
        }
        else{
            throw new IllegalStateException("No puedes eliminar la inscripción cuando el curso ya comenzó.");
        }
    }

    @Override
    public List<CursoDTO> getCursosDisponibles(Long idAsignatura, String name) {
        // Buscar al Estudiante
        Optional<Usuario> usuario = usuarioRepository.findByEmail(name);
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        Estudiante estudiante = (Estudiante) usuario.get();

        // Buscar la asignatura
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        // Listar los cursos de la asignatura
        List<Curso> cursos = asignatura.getCursos();

        // Crear la lista de cursos disponibles
        List<CursoDTO> cursosDisponibles = new ArrayList<>();

        // Fecha actual
        LocalDate hoy = LocalDate.now();

        // Verificar si el estudiante está inscrito en algún curso de la asignatura en el período actual
        List<InscripcionCurso> inscripcionesActuales = inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(estudiante.getId(), idAsignatura);

        // Verificar si alguna de las inscripciones actuales no está completada
        for (InscripcionCurso inscripcion : inscripcionesActuales) {
            if (inscripcion.getEstado() != EstadoInscripcionCurso.COMPLETADA) {
                return cursosDisponibles;  // Devuelve una lista vacía porque ya está inscrito en un curso no completado de la asignatura
            }
            if(inscripcion.getCalificacion().equals(CalificacionCurso.EXONERADO)){
                return cursosDisponibles;  // Devuelve una lista vacía porque ya exoneró el curso
            }
        }

        // Verificar los cursos disponibles para inscripción
        for (Curso curso : cursos) {
            LocalDate fechaInicioCurso = curso.getFechaInicio();
            LocalDate fechaFinInscripcion = curso.getFechaInicio().minusDays(curso.getDiasPrevInsc());

            // Verificar que la fecha actual esté dentro del rango de inscripción
            if (!hoy.isAfter(fechaInicioCurso) && !hoy.isBefore(fechaFinInscripcion)) {
                CursoDTO cursoDTO = modelMapper.map(curso, CursoDTO.class);
                cursosDisponibles.add(cursoDTO);
            }
        }

        return cursosDisponibles;
    }

}