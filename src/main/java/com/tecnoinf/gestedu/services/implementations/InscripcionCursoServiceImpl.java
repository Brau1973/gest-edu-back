package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.InscripcionCursoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionCursoServiceImpl implements InscripcionCursoService {
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final InscripcionCarreraRepository inscripcionCarreraRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public InscripcionCursoServiceImpl(CursoRepository cursoRepository, EstudianteRepository estudianteRepository, InscripcionCursoRepository inscripcionCursoRepository, InscripcionCarreraRepository inscripcionCarreraRepository, AsignaturaRepository asignaturaRepository, ModelMapper modelMapper) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.modelMapper = modelMapper;
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
}