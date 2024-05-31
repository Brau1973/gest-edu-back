package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.examen.CreateExamenDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.CreateInscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.inscripcionExamen.InscripcionExamenDTO;
import com.tecnoinf.gestedu.dtos.periodoExamen.PeriodoExamenDTO;
import com.tecnoinf.gestedu.exceptions.*;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.CarreraService;
import com.tecnoinf.gestedu.services.interfaces.ExamenService;
import com.tecnoinf.gestedu.services.interfaces.PeriodoExamenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamenServiceImpl implements ExamenService {

    private static final int DIAS_PREV_INSC_DEFAULT = 10;

    private final CarreraService carreraService;
    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final ExamenRepository examenRepository;
    private final UsuarioRepository usuarioRepository;
    private final InscripcionExamenRepository inscripcionExamenRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;

    public ExamenServiceImpl(PeriodoExamenService periodoExamenService, CarreraService carreraService,
                             AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository,
                             ExamenRepository examenRepository, UsuarioRepository usuarioRepository, InscripcionExamenRepository inscripcionExamenRepository,
                             InscripcionCursoRepository inscripcionCursoRepository, EstudianteRepository estudianteRepository){
        this.carreraService = carreraService;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.examenRepository = examenRepository;
        this.usuarioRepository = usuarioRepository;
        this.inscripcionExamenRepository = inscripcionExamenRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;

    }

    @Override
    public ExamenDTO altaExamen(CreateExamenDTO createExamenDto) {
        Asignatura asignatura = obtenerAsignatura(createExamenDto.getAsignaturaId());

        List<Docente> docentes = new ArrayList<>();
        for (Long docenteId : createExamenDto.getDocenteIds()) {
            Docente docente = docenteRepository.findById(docenteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Docente con id " + docenteId + " no encontrado"));
            docentes.add(docente);
        }
        if (docentes.isEmpty()) {
            throw new ResourceNotFoundException("Se requiere al menos un docente para crear un examen.");
        }

        LocalDateTime fechaExamen = LocalDateTime.parse(createExamenDto.getFecha());

        validarFechaExamen(fechaExamen, asignatura);

        Examen examen = crearExamen(createExamenDto, asignatura, docentes);
        examenRepository.save(examen);
        return new ExamenDTO(examen);
    }

    private Asignatura obtenerAsignatura(Long asignaturaId) {
        if (asignaturaId == null) {
            throw new ResourceNotFoundException("Se requiere una asignatura para crear un examen.");
        }
        return asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada."));
    }

    private void validarFechaExamen(LocalDateTime fechaExamen, Asignatura asignatura) {
        LocalDateTime fecha = fechaExamen.withSecond(0).withNano(0);
        if (examenRepository.existsByFechaAndAsignatura(fecha, asignatura)) {
            throw new UniqueFieldException("Ya existe un examen programado para esta asignatura en la misma fecha y hora.");
        }
        List<PeriodoExamenDTO> periodosExamen = carreraService.obtenerPeriodosExamenCarrera(asignatura.getCarrera().getId(), Pageable.unpaged()).getContent();
        if (!isFechaDentroDePeriodo(fecha, periodosExamen)) {
            throw new FechaException("La fecha del examen no está dentro de un periodo de examen vigente.");
        }
    }

    private Examen crearExamen(CreateExamenDTO createExamenDto, Asignatura asignatura, List<Docente> docentes) {

        LocalDateTime fechaExamen = LocalDateTime.parse(createExamenDto.getFecha());

        Examen examen = new Examen();
        examen.setFecha(fechaExamen.withSecond(0).withNano(0));
        examen.setDiasPrevInsc(createExamenDto.getDiasPrevInsc() != null ? createExamenDto.getDiasPrevInsc() : DIAS_PREV_INSC_DEFAULT);
        examen.setAsignatura(asignatura);
        examen.setDocentes(docentes);
        return examen;
    }

    private boolean isFechaDentroDePeriodo(LocalDateTime fechaExamen, List<PeriodoExamenDTO> periodosExamen) {
        return periodosExamen.stream().anyMatch(periodoExamen -> {
            LocalDateTime fechaInicio = LocalDateTime.parse(periodoExamen.getFechaInicio());
            LocalDateTime fechaFin = LocalDateTime.parse(periodoExamen.getFechaFin());
            return (fechaExamen.isAfter(fechaInicio) || fechaExamen.isEqual(fechaInicio)) &&
                    (fechaExamen.isBefore(fechaFin) || fechaExamen.isEqual(fechaFin));
        });
    }

    @Override
    public InscripcionExamenDTO inscribirseExamen(CreateInscripcionExamenDTO inscripcionExamenDto){
        Optional<Usuario> usuario = usuarioRepository.findByEmail(inscripcionExamenDto.getEmail());
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        Examen examen = examenRepository.findById(inscripcionExamenDto.getExamenId())
                .orElseThrow(() -> new ResourceNotFoundException("Examen no encontrado."));

        if (!isEnPeriodoInscripcion(examen)) {
            throw new PeriodoInscripcionExeption("El examen no está en período de inscripción.");
        }

        if (isEstudianteInscripto(examen, usuario.get())) {
            throw new InscripcionExamenException("El estudiante ya está inscripto en el examen.");
        }

        if(isEstudianteAprobado(examen.getAsignatura(), usuario.get())){
            throw new InscripcionExamenException("El estudiante ya aprobó la asignatura.");
        }
        if(!isEstudianteAExamen(examen.getAsignatura(), usuario.get())){
            throw new InscripcionExamenException("El estudiante no está en condición de rendir examen.");
        }

        InscripcionExamen inscripcion = new InscripcionExamen();
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setCalificacion(CalificacionExamen.PENDIENTE);
        inscripcion.setEstudiante((Estudiante) usuario.get());
        inscripcion.setExamen(examen);
        inscripcionExamenRepository.save(inscripcion);
        return new InscripcionExamenDTO(inscripcion);
    }

    private boolean isEnPeriodoInscripcion(Examen examen) {
        LocalDateTime fechaActual = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fechaInscripcion = examen.getFecha().minusDays(examen.getDiasPrevInsc()).withHour(0).withMinute(0).withSecond(0).withNano(0);;
        return !fechaActual.isBefore(fechaInscripcion) && fechaActual.isBefore(examen.getFecha());
    }

    private boolean isEstudianteInscripto(Examen examen, Usuario usuario) {
        return examen.getInscripciones().stream().anyMatch(inscripcion -> inscripcion.getEstudiante().equals(usuario));
    }

    private boolean isEstudianteAprobado(Asignatura asignatura, Usuario usuario){
        List<InscripcionExamen> examenesInscripto =  inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(usuario.getId(), asignatura.getId());
        for(InscripcionExamen inscripcion : examenesInscripto){
            if(inscripcion.getCalificacion() == CalificacionExamen.APROBADO){
                return true;
            }
        }
        List<InscripcionCurso> cursosInscripto =  inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(usuario.getId(), asignatura.getId());
        for(InscripcionCurso inscripcion : cursosInscripto){
            if(inscripcion.getCalificacion().equals(CalificacionCurso.EXONERADO)){
                return true;
            }
        }
        return false;
    }

    private boolean isEstudianteAExamen(Asignatura asignatura, Usuario usuario){
        List<InscripcionCurso> cursosInscripto =  inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(usuario.getId(), asignatura.getId());
        for(InscripcionCurso inscripcion : cursosInscripto){
            if(inscripcion.getCalificacion().equals(CalificacionCurso.AEXAMEN)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<InscripcionExamenDTO> listarInscriptosExamen(Long id){
        Examen examen = examenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen no encontrado."));
        return examen.getInscripciones()
                .stream()
                .map(inscripcion -> new InscripcionExamenDTO(inscripcion))
                .toList();
    }

    @Override
    public Page<ExamenDTO> listarExamenesPendientes(Pageable pageable){
        return examenRepository.findAllByFechaBeforeAndEstado(LocalDateTime.now(), Estado.ACTIVO, pageable)
                .map(ExamenDTO::new);
    }

    @Override
    public List<InscripcionExamenCalificacionDTO> obtenerCalificaciones(Long id){
        Examen examen = examenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen no encontrado."));
        return examen.getInscripciones()
                .stream()
                .map(InscripcionExamenCalificacionDTO::new)
                .toList();
    }

    @Override
    public List<InscripcionExamenCalificacionDTO> registrarCalificaciones(Long id, List<InscripcionExamenCalificacionDTO> calificaciones) {
        Examen examen = examenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen no encontrado."));
        if (calificaciones.isEmpty()) {
            throw new CalificacionExamenExeption("No se han enviado calificaciones para registrar.");
        }
        if (examen.getEstado() == Estado.FINALIZADO) {
            throw new CalificacionExamenExeption("El examen ya tiene calificaciones.");
        }
        if (examen.getInscripciones().size() != calificaciones.size()) {
            throw new CalificacionExamenExeption("No todos los estudiantes tienen una calificación.");
        }

        for (InscripcionExamenCalificacionDTO calificacionDTO : calificaciones) {
            if(calificacionDTO.getCalificacion() == null){
                throw new CalificacionExamenExeption("La calificación no puede ser nula.");
            }
            if(calificacionDTO.getCalificacion() == CalificacionExamen.PENDIENTE){
                throw new CalificacionExamenExeption("La calificación no puede ser PENDIENTE.");
            }
            Optional<InscripcionExamen> inscripcionOpt = inscripcionExamenRepository.findByEstudianteIdAndExamenId(calificacionDTO.getEstudianteId(), id);

            if (!inscripcionOpt.isPresent()) {
                throw new ResourceNotFoundException("No se encontró la inscripción del estudiante id " + calificacionDTO.getEstudianteId());
            }

            InscripcionExamen inscripcion = inscripcionOpt.get();
            inscripcion.setCalificacion(calificacionDTO.getCalificacion());
            inscripcionExamenRepository.save(inscripcion);
        }
        examen.setEstado(Estado.FINALIZADO);
        examenRepository.save(examen);

        return examen.getInscripciones()
                .stream()
                .map(InscripcionExamenCalificacionDTO::new)
                .toList();
    }

}