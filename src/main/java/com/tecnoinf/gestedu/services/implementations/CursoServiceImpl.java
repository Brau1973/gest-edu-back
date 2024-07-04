package com.tecnoinf.gestedu.services.implementations;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.curso.ActaCursoDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.curso.HorarioDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoCalificacionDTO;
import com.tecnoinf.gestedu.dtos.inscripcionCurso.InscripcionCursoDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoEstudianteDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Horario;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.DocenteRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.HorarioRepository;
import com.tecnoinf.gestedu.repositories.InscripcionCursoRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.CursoService;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final HorarioRepository horarioRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final ModelMapper modelMapper;
    private final ActividadService actividadService;

    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, ActividadService actividadService , AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository, EstudianteRepository estudianteRepository, HorarioRepository horarioRepository, InscripcionCursoRepository inscripcionCursoRepository, ModelMapper modelMapper) {
        this.cursoRepository = cursoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.horarioRepository = horarioRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.modelMapper = modelMapper;
        this.actividadService = actividadService;
    }

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO) {
        Asignatura asignatura = asignaturaRepository.findById(cursoDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + cursoDTO.getAsignaturaId()));

        // Validar que la fecha de inicio sea menor a la fecha de un mes en el futuro
        //LocalDate unMesEnElFuturo = LocalDate.now().plusMonths(1);

        //if (cursoDTO.getFechaInicio().isBefore(unMesEnElFuturo)) {
            //throw new IllegalArgumentException("La fecha de inicio no puede ser mayor a la fecha de un mes en el futuro.");
        //}

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

            actividadService.registrarActividad(TipoActividad.ALTA_CURSO, "Se ha creado un curso para la asignatura" + asignatura.getNombre() + " con fecha de inicio " + curso.getFechaInicio() + " y fecha de fin " + curso.getFechaFin());

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
        List<Horario> horariosEnSemestre = horarioRepository.findHorariosBySemestreDiaAndCarrera(curso.getAsignatura().getSemestrePlanEstudio(), nuevoHorario.getDia(), curso.getAsignatura().getCarrera().getId());
        if (horarioSolapa(horariosEnSemestre, nuevoHorario)) {
            throw new IllegalArgumentException("El horario se solapa con otro horario existente en el mismo semestre.");
        }

        Horario horario = modelMapper.map(nuevoHorario, Horario.class);
        horario.setCurso(curso);
        curso.getHorarios().add(horario);
        horarioRepository.save(horario);
        cursoRepository.save(curso);

        actividadService.registrarActividad(TipoActividad.REGISTRO_HORARIOS_CURSO, "Se ha registrado un horario para el curso con id " + cursoId);

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

    // Mapeo manual para propósitos de prueba
    List<UsuarioDTO> usuariosDTO = estudiantes.stream()
            .map(estudiante -> modelMapper.map(estudiante, UsuarioDTO.class))
            .collect(Collectors.toList());

    return usuariosDTO;
}

    @Override
    public CursoDTO getCursoPorId(Long cursoId){
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + cursoId));

        Type listType = new TypeToken<CursoDTO>(){}.getType();
        return modelMapper.map(curso, listType);
    }

    @Override
    public Page<HorarioDTO> getHorariosByCurso(Long cursoId, Pageable pageable)
    {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with id " + cursoId));
        List<Horario> horarios = curso.getHorarios();
        List<HorarioDTO> listaHorarioDTO = new ArrayList<>();

        if(horarios != null){
            for(Horario aux : horarios){
                HorarioDTO horarioDTO = new HorarioDTO();
                horarioDTO.setId(aux.getId());
                horarioDTO.setDia(aux.getDia());
                horarioDTO.setHoraInicio(aux.getHoraInicio());
                horarioDTO.setHoraFin(aux.getHoraFin());
                listaHorarioDTO.add(horarioDTO);
            }
        }
        return new PageImpl<>(listaHorarioDTO, pageable, listaHorarioDTO.size());
        //Type listType = new TypeToken<List<HorarioDTO>>(){}.getType();
        //return modelMapper.map(horarios, listType);
    }

    @Override
    public List<InscripcionCursoCalificacionDTO> obtenerCalificaciones(Long id){
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        return curso.getInscripciones()
                .stream()
                .map(InscripcionCursoCalificacionDTO::new)
                .toList();
    }

    @Override
    public ActaCursoDTO generarActaCurso(Long id) {
        Curso curso;
        try {
            curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));
        } catch (Exception e) {
            System.err.println("Error al obtener el curso: " + e.getMessage());
            throw e;
        }

        ActaCursoDTO actaCurso = new ActaCursoDTO();
        try {
            actaCurso.setId(curso.getId());
            actaCurso.setFecha(curso.getFechaFin());
            actaCurso.setAsignatura(new AsignaturaDTO(curso.getAsignatura()));
            actaCurso.setDocente(new DocenteDTO(curso.getDocente()));
            actaCurso.setCurso(new CursoDTO(curso));
            actaCurso.setInscripciones(curso.getInscripciones().stream().map(InscripcionCursoDTO::new).toList());
            List<BasicInfoEstudianteDTO> listaEstudiantes = new ArrayList<>();
            List<InscripcionCursoDTO> inscripciones = actaCurso.getInscripciones();
            for(InscripcionCursoDTO inscripcion: inscripciones){
                Usuario estudiante = estudianteRepository.findById(inscripcion.getEstudianteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));
                BasicInfoEstudianteDTO infoEstudiante = new BasicInfoEstudianteDTO();
                infoEstudiante.setId(estudiante.getId());
                infoEstudiante.setCi(estudiante.getCi());
                infoEstudiante.setNombre(estudiante.getNombre());
                infoEstudiante.setApellido(estudiante.getApellido());
                listaEstudiantes.add(infoEstudiante);
            }
            actaCurso.setEstudiantes(listaEstudiantes);
        } catch (Exception e) {
            System.err.println("Error al crear Acta Curso: " + e.getMessage());
            throw e;
        }

        try {
            actividadService.registrarActividad(TipoActividad.GENERACION_ACTA_CURSO, "Generación del acta del curso con id " + id);
        } catch (Exception e) {
            System.err.println("Error al registrar actividad: " + e.getMessage());
            throw e;
        }

        return actaCurso;
    }

}