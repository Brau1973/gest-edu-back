package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.curso.CursoHorarioDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import com.tecnoinf.gestedu.exceptions.CalificacionCursoException;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.*;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.EmailService;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCursoService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @Autowired
    public InscripcionCursoServiceImpl(CursoRepository cursoRepository, EstudianteRepository estudianteRepository,
                                       InscripcionCursoRepository inscripcionCursoRepository, InscripcionCarreraRepository inscripcionCarreraRepository,
                                       AsignaturaRepository asignaturaRepository, UsuarioRepository usuarioRepository, EmailService emailService, ModelMapper modelMapper,
                                       ActividadService actividadService) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.actividadService = actividadService;
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
    public InscripcionCursoDTO createInscripcionCurso(InscripcionCursoDTO inscripcionCursoDTO) {
        Usuario estudiante = estudianteRepository.findById(inscripcionCursoDTO.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante not found with id " + inscripcionCursoDTO.getEstudianteId()));

        Curso curso = cursoRepository.findById(inscripcionCursoDTO.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + inscripcionCursoDTO.getCursoId()));

        Estudiante auxEstudiante = (Estudiante) estudiante;

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
                            inscripcionCurso.setEstudiante((Estudiante) estudiante);
                            inscripcionCurso.setCurso(curso);
                            inscripcionCurso.setFechaInscripcion(LocalDateTime.now());
                            InscripcionCurso createdInscripcion = inscripcionCursoRepository.save(inscripcionCurso);
                            return modelMapper.map(createdInscripcion, InscripcionCursoDTO.class);
                        }
                        //Si tiene previas...
                        else {
                            //Tengo que chequear que las haya pasado
                            if (exoneroCursosPrevios(asignaturasPrevias, inscripcionCursos)) {
                                InscripcionCurso inscripcionCurso = new InscripcionCurso();
                                inscripcionCurso.setCalificacion(CalificacionCurso.PENDIENTE);
                                inscripcionCurso.setEstudiante((Estudiante) estudiante);
                                inscripcionCurso.setCurso(curso);
                                inscripcionCurso.setFechaInscripcion(LocalDateTime.now());
                                InscripcionCurso createdInscripcion = inscripcionCursoRepository.save(inscripcionCurso);

                                actividadService.registrarActividad(TipoActividad.INSCRIPCION_A_CURSO, "Inscripcion a curso exitoso");

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
    public List<InscripcionCursoCalificacionDTO> registrarCalificaciones(Long id, List<InscripcionCursoCalificacionDTO> calificaciones){
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

            //TODO Chequear el mandado de mails
            /*try {
                emailService.sendCalificacionCursoEmail("gestedu.info@gmail.com", estudiante.getNombre(), inscripcionCurso.getCurso().getAsignatura().getNombre(), inscripcionCurso.getCalificacion());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }*/
        }

        curso.setEstado(Estado.FINALIZADO);
        cursoRepository.save(curso);

        return curso.getInscripciones()
                .stream()
                .map(InscripcionCursoCalificacionDTO::new)
                .toList();
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
    public List<CursoHorarioDTO> listarCursosHorariosInscriptos(Long idEstudiante){
        //La lista que voy a devolver al final
        List<CursoHorarioDTO> cursoHorarioDTOS = new ArrayList<>();
        //Busco todas las inscripciones de un alumno
        List<InscripcionCurso> inscripcionCursos = inscripcionCursoRepository.findInscripcionCursoEstudianteById(idEstudiante);
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
                auxCursoHorario.setFechaInicio(curso.getFechaInicio());
                auxCursoHorario.setFechaFin(curso.getFechaFin());
                auxCursoHorario.setEstado(curso.getEstado());
                auxCursoHorario.setDocenteNombre(curso.getDocente().getNombre());
                auxCursoHorario.setDocenteApellido(curso.getDocente().getApellido());
                auxCursoHorario.setAsignaturaNombre(curso.getAsignatura().getNombre());
                auxCursoHorario.setDiasPrevInsc(curso.getDiasPrevInsc());

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
}