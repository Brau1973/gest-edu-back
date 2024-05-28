package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CursoServiceImpl implements CursoService{
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final InscripcionACursoRepository inscripcionACursoRepository;
    private final HorarioRepository horarioRepository;
    private final ModelMapper modelMapper;



    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository, EstudianteRepository estudianteRepository, InscripcionACursoRepository inscripcionACursoRepository, HorarioRepository horarioRepository, ModelMapper modelMapper) {
        this.cursoRepository = cursoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.inscripcionACursoRepository = inscripcionACursoRepository;
        this.horarioRepository = horarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO, HorarioDTO horarioDTO, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(cursoDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + cursoDTO.getAsignaturaId()));

        if (cursoDTO.getFechaInicio().compareTo(cursoDTO.getFechaFin()) < 0) {
            Curso curso = modelMapper.map(cursoDTO, Curso.class);
            curso.setAsignaturas(asignatura);
            curso.setId(null);

            if (horarioDTO.getHoraInicio().compareTo(horarioDTO.getHoraFin()) < 0) {
                Horario horario = modelMapper.map(horarioDTO, Horario.class);
                curso.addHorario(horario);
                boolean existeDocente = docenteRepository.existsById(docenteId);
                if(existeDocente){
                    Docente docente = docenteRepository.getById(docenteId);
                    curso.addDocente(docente);
                    Horario createdHorario = horarioRepository.save(horario);
                    modelMapper.map(createdHorario, HorarioDTO.class);
                    Curso createdCurso = cursoRepository.save(curso);
                    return modelMapper.map(createdCurso, CursoDTO.class);
                } else {
                    throw new IllegalArgumentException("El docente no existe");
                }
            } else {
                throw new IllegalArgumentException("Hora de inicio debe ser anterior a la hora de fin");
            }
        } else {
            throw new IllegalArgumentException("La fecha inicial debe ser anterior a la fecha final");
        }
    }
}
