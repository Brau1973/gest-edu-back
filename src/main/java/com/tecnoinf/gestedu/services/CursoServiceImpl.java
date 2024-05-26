package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Horario;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceImpl implements CursoService{
    private final CursoRepository cursoRepository;
    private final ModelMapper modelMapper;
    private final AsignaturaRepository asignaturaRepository;

    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, AsignaturaRepository asignaturaRepository , ModelMapper modelMapper) {
        this.cursoRepository = cursoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO, HorarioDTO horarioDTO, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(cursoDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + cursoDTO.getAsignaturaId()));

        Curso curso = new Curso();
        curso = modelMapper.map(cursoDTO, Curso.class);
        curso.setAsignaturas(asignatura);
        curso.setId(null);

        for (HorarioDTO h : cursoDTO.getHorarios()) {
            Horario horario = modelMapper.map(h, Horario.class);
            curso.addHorario(horario);
        }

        Curso createdCurso = cursoRepository.save(curso);
        return modelMapper.map(createdCurso, CursoDTO.class);
    }
}
