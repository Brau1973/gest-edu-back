package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.curso.InscripcionACursoDTO;
import com.tecnoinf.gestedu.enumerados.CalificacionCursada;
import com.tecnoinf.gestedu.enumerados.EstadoInscACurso;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class CursoServiceImpl implements CursoService{
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final InscripcionACursoRepository inscripcionACursoRepository;
    private final ModelMapper modelMapper;



    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository, EstudianteRepository estudianteRepository, InscripcionACursoRepository inscripcionACursoRepository, ModelMapper modelMapper) {
        this.cursoRepository = cursoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.inscripcionACursoRepository = inscripcionACursoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CursoDTO createCurso(HorarioCursoDTO horarioCursoDTO){//CursoDTO cursoDTO, HorarioDTO horarioDTO, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(horarioCursoDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + horarioCursoDTO.getAsignaturaId()));

        if (horarioCursoDTO.getFechaInicio().compareTo(horarioCursoDTO.getFechaFin()) < 0) {
            Curso curso = new Curso();
            curso = modelMapper.map(horarioCursoDTO, Curso.class);
            curso.setAsignaturas(asignatura);
            curso.setId(null);

            HorarioDTO h = new HorarioDTO();
            h.setId(horarioCursoDTO.getIdHorario());
            h.setDia(horarioCursoDTO.getDia());
            h.setHoraInicio(horarioCursoDTO.getHoraInicio());
            h.setHoraFin(horarioCursoDTO.getHoraFin());
            h.addCursosId(horarioCursoDTO.getIdCurso());
            if (h.getHoraInicio().compareTo(h.getHoraFin()) < 0) {
                Horario horario = modelMapper.map(h, Horario.class);
                curso.addHorario(horario);
            } else {
                throw new IllegalArgumentException("Hora de inicio debe ser anterior a la hora de fin");
            }

            boolean existeDocente = docenteRepository.existsById(horarioCursoDTO.getIdDocente());

            if(existeDocente){
                Docente docente = docenteRepository.getById(horarioCursoDTO.getIdDocente());
                curso.addDocente(docente);

                Curso createdCurso = cursoRepository.save(curso);
                return modelMapper.map(createdCurso, CursoDTO.class);
            }
                else{
                throw new IllegalArgumentException("El docente no existe");
            }
        }
        else {
            throw new IllegalArgumentException("La fecha inicial debe ser anterior a la fecha final");
        }
    }

    @Override
    public boolean isEstudianteInscritoEnCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteId);

        Curso curso = new Curso();
        curso.setId(cursoId);

        return inscripcionACursoRepository.existsByEstudianteAndCurso(estudiante, curso);
    }

    @Override
    public List<InscripcionACursoDTO> listarInscripcionesPorEstudiante(Long estudianteId) {
        List<InscripcionACurso> inscripciones = inscripcionACursoRepository.findByEstudianteId(estudianteId);
        List<InscripcionACursoDTO> inscripcionACursoDTOList = null;
        for(InscripcionACurso ins: inscripciones){
            InscripcionACursoDTO auxInscripcion = new InscripcionACursoDTO();
            auxInscripcion.setId(ins.getId());
            auxInscripcion.setEstadoInscACurso(ins.getEstadoInscACurso());
            auxInscripcion.setCalificacion(ins.getCalificacion());
            auxInscripcion.setFechaInscripcion(ins.getFechaInscripcion());
            auxInscripcion.setIdEstudiante(ins.getEstudiante().getId());
            auxInscripcion.setIdCurso(ins.getCurso().getId());
            inscripcionACursoDTOList.add(auxInscripcion);
        }
        return inscripcionACursoDTOList;
    }

    @Override
    public InscripcionACursoDTO inscribirCurso(InscripcionACursoDTO inscripcionCurso){
        Optional<Usuario> optionalEstudiante = estudianteRepository.findById(inscripcionCurso.getIdEstudiante());
        Estudiante estudiante = (Estudiante) optionalEstudiante.orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado"));

        List<InscripcionACursoDTO> inscripcionACursoDTOList = listarInscripcionesPorEstudiante(inscripcionCurso.getIdEstudiante());
        if(inscripcionACursoDTOList.isEmpty()){
            //TODO fijarse un sistema para las previas
            InscripcionACurso insc = new InscripcionACurso();
            insc = modelMapper.map(inscripcionCurso, InscripcionACurso.class);
            insc.setId(null);
            insc.setCalificacion(CalificacionCursada.PENDIENTE);
            insc.setEstudiante(estudiante);

            insc.setEstadoInscACurso(EstadoInscACurso.CURSANDO);
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            insc.setFechaInscripcion(date);


            insc.setCurso(cursoRepository.getReferenceById(inscripcionCurso.getIdCurso()));
            InscripcionACurso createdInscripcion = inscripcionACursoRepository.save(insc);
            return modelMapper.map(createdInscripcion, InscripcionACursoDTO.class);
        }
        else{
            for(InscripcionACursoDTO auxInsc: inscripcionACursoDTOList){
                if(auxInsc.equals(inscripcionCurso.getIdCurso())){
                    if(auxInsc.getCalificacion().equals(CalificacionCursada.EXONERADO)){
                        throw new IllegalArgumentException("No puedes inscribirte a un curso exonerado.");
                    }
                    else{
                        //TODO fijarse un sistema para las previas
                        InscripcionACurso insc = new InscripcionACurso();
                        insc = modelMapper.map(inscripcionCurso, InscripcionACurso.class);
                        insc.setId(null);
                        insc.setCalificacion(CalificacionCursada.PENDIENTE);
                        insc.setEstudiante(estudiante);

                        insc.setEstadoInscACurso(EstadoInscACurso.CURSANDO);
                        LocalDate localDate = LocalDate.now();
                        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        insc.setFechaInscripcion(date);


                        insc.setCurso(cursoRepository.getReferenceById(inscripcionCurso.getIdCurso()));
                        InscripcionACurso createdInscripcion = inscripcionACursoRepository.save(insc);
                        return modelMapper.map(createdInscripcion, InscripcionACursoDTO.class);
                    }
                }
            }
            throw new IllegalArgumentException("Error al inscribirse al curso");
        }
    }
}
