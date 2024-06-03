package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.CursoService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final HorarioRepository horarioRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository, EstudianteRepository estudianteRepository, HorarioRepository horarioRepository, InscripcionCursoRepository inscripcionCursoRepository, ModelMapper modelMapper) {
        this.cursoRepository = cursoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.horarioRepository = horarioRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO) {
        Asignatura asignatura = asignaturaRepository.findById(cursoDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + cursoDTO.getAsignaturaId()));

        // Validar que la fecha de inicio sea menor a la fecha de un mes en el futuro
        LocalDate unMesEnElFuturo = LocalDate.now().plusMonths(1);

        if (cursoDTO.getFechaInicio().isBefore(unMesEnElFuturo)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser mayor a la fecha de un mes en el futuro.");
        }

        if (cursoDTO.getFechaInicio().isBefore(cursoDTO.getFechaFin())) {
            Curso curso = new Curso();
            curso.setFechaInicio(cursoDTO.getFechaInicio());
            curso.setFechaFin(cursoDTO.getFechaFin());
            curso.setDiasPrevInsc(cursoDTO.getDiasPrevInsc());
            curso.setEstado(Estado.ACTIVO);
            curso.setAsignatura(asignatura);
            /*Curso curso = modelMapper.map(cursoDTO, Curso.class);
            curso.setAsignatura(asignatura);
            curso.setId(null);
            curso.setEstado(Estado.ACTIVO);*/
            Docente docente = docenteRepository.findById(cursoDTO.getDocenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Docente not found with id " + cursoDTO.getDocenteId()));
            curso.setDocente(docente);
            Curso createdCurso = cursoRepository.save(curso);
            return modelMapper.map(createdCurso, CursoDTO.class);

        } else {
            throw new IllegalArgumentException("La fecha inicial debe ser anterior a la fecha final.");
        }
    }

    public boolean horarioSolapa(List<Horario> horarios, HorarioDTO nuevoHorario) {
        for (Horario horario : horarios) {
            if (horario.getDia() == nuevoHorario.getDia() &&
                    horariosSeSolapan(horario.getHoraInicio(), horario.getHoraFin(), nuevoHorario.getHoraInicio(), nuevoHorario.getHoraFin())) {
                return true;
            }
        }
        return false;
    }

    private boolean horariosSeSolapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return (inicio1.isBefore(fin2) && inicio2.isBefore(fin1));
    }

    @Override
    public HorarioDTO addHorarioToCurso(Long cursoId, HorarioDTO nuevoHorario) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + cursoId));
        List<Horario> horariosEnSemestre = horarioRepository.findHorariosBySemestreAndDia(curso.getAsignatura().getSemestrePlanEstudio(), nuevoHorario.getDia());
        if (horarioSolapa(horariosEnSemestre, nuevoHorario)) {
            throw new IllegalArgumentException("El horario se solapa con otro horario existente en el mismo semestre.");
        }

        Horario horario = modelMapper.map(nuevoHorario, Horario.class);
        horario.setCurso(curso);
        curso.getHorarios().add(horario);
        horarioRepository.save(horario);
        cursoRepository.save(curso);

        return modelMapper.map(horario, HorarioDTO.class);
    }

    @Override
    public List<UsuarioDTO> getEstudiantesByCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + cursoId));
        List<InscripcionCurso> inscripciones = curso.getInscripciones();
        List<Estudiante> estudiantes = new ArrayList<>();
        for (InscripcionCurso inscripcionCurso : inscripciones) {
            estudiantes.add(inscripcionCurso.getEstudiante());
        }

        Type listType = new TypeToken<List<UsuarioDTO>>() {
        }.getType();
        return modelMapper.map(estudiantes, listType);
    }

    @Override
    public CursoDTO getCursoPorId(Long cursoId){
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + cursoId));

        Type listType = new TypeToken<CursoDTO>(){}.getType();
        return modelMapper.map(curso, listType);
    }
}