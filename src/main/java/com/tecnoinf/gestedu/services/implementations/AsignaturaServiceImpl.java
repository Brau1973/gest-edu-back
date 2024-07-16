package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.asignatura.CreateAsignaturaDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.carrera.CreateCarreraDTO;
import com.tecnoinf.gestedu.dtos.curso.CursoDTO;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.exceptions.*;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Curso;
import com.tecnoinf.gestedu.models.Examen;
import com.tecnoinf.gestedu.models.InscripcionCurso;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.Estado;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.AsignaturaRepository;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.CursoRepository;
import com.tecnoinf.gestedu.repositories.ExamenRepository;
import com.tecnoinf.gestedu.repositories.InscripcionCursoRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import com.tecnoinf.gestedu.services.interfaces.AsignaturaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final ModelMapper modelMapper;
    private final CarreraRepository carreraRepository;
    private final ExamenRepository examenRepository;
    private final ActividadService actividadService;
    private final CursoRepository cursoRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AsignaturaServiceImpl(ActividadService actividadService, AsignaturaRepository asignaturaRepository, CarreraRepository carreraRepository ,
                                 ModelMapper modelMapper, ExamenRepository examenRepository, CursoRepository cursoRepository, InscripcionCursoRepository inscripcionCursoRepository,
                                 UsuarioRepository usuarioRepository) {
        this.asignaturaRepository = asignaturaRepository;
        this.carreraRepository = carreraRepository;
        this.modelMapper = modelMapper;
        this.examenRepository = examenRepository;
        this.actividadService = actividadService;
        this.cursoRepository = cursoRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public AsignaturaDTO createAsignatura(CreateAsignaturaDTO createAsignaturaDto) {
        Carrera carrera = carreraRepository.findById(createAsignaturaDto.getCarreraId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + createAsignaturaDto.getCarreraId()));

        if (asignaturaRepository.existsByNombreAndCarreraId(createAsignaturaDto.getNombre(), carrera.getId())) {
            throw new UniqueFieldException("Ya existe una asignatura con el nombre " + createAsignaturaDto.getNombre() + " en la carrera  " + carrera.getNombre() + " (id Carrera: " + carrera.getId() + ")");
        }
        Asignatura asignatura = new Asignatura();
        asignatura = modelMapper.map(createAsignaturaDto, Asignatura.class);
        asignatura.setCarrera(carrera);
        asignatura.setId(null);
        Asignatura createdAsignatura = asignaturaRepository.save(asignatura);

        actividadService.registrarActividad(TipoActividad.ALTA_ASIGNATURA, "Se ha creado la asignatura " + createdAsignatura.getNombre() + " en la carrera " + carrera.getNombre() + " (id Carrera: " + carrera.getId() + ")");

        return modelMapper.map(createdAsignatura, AsignaturaDTO.class);
    }

    @Override
    public AsignaturaDTO updateAsignatura(Long id, CreateAsignaturaDTO createAsignaturaDTO) {
        return asignaturaRepository.findById(id)
                .map(existingAsignatura -> {
                    if (createAsignaturaDTO.getNombre() != null && !createAsignaturaDTO.getNombre().isEmpty()) {
                        existingAsignatura.setNombre(createAsignaturaDTO.getNombre());
                    }
                    if (createAsignaturaDTO.getDescripcion() != null && !createAsignaturaDTO.getDescripcion().isEmpty()) {
                        existingAsignatura.setDescripcion(createAsignaturaDTO.getDescripcion());
                    }
                    if (createAsignaturaDTO.getCreditos() != null && createAsignaturaDTO.getCreditos() > 0) {
                        existingAsignatura.setCreditos(createAsignaturaDTO.getCreditos());
                    }
                    Asignatura updatedAsignatura = asignaturaRepository.save(existingAsignatura);

                    actividadService.registrarActividad(TipoActividad.EDITAR_ASIGNATURA, "Se ha editado la asignatura con id " + id);

                    return modelMapper.map(updatedAsignatura, AsignaturaDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + id));
    }

    @Override
    public List<AsignaturaDTO> getPrevias(Long asignaturaId){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + asignaturaId));
        List<Asignatura> previas = asignatura.getPrevias();
        Type listType = new TypeToken<List<AsignaturaDTO>>(){}.getType();
        return modelMapper.map(previas, listType);
    }

    @Override
    public AsignaturaDTO addPrevia(Long asignaturaId, Long previaId) {

        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura id " + asignaturaId + "no se encuentra"));
        Asignatura previa = asignaturaRepository.findById(previaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura id " + previaId + "no se encuentra"));

        if(!asignatura.getCarrera().getExistePlanEstudio()){
            throw new PlanEstudioNoExisteException("Primero debe ingresar un plan de estudio en la carrera.");
        }
        if(asignatura.getSemestrePlanEstudio()==0 || asignatura.getSemestrePlanEstudio()==1){
            throw new SemestreException("No se puede agregar una asignatura previa a una asignatura del primer semestre");
        }
        if(asignatura.getSemestrePlanEstudio() <= previa.getSemestrePlanEstudio()){
            throw new SemestreException("No se puede agregar una previa del mismo semestre o de un semestre mayor.");
        }
        if(asignatura.getPrevias().contains(previa)){
            throw new AsignaturaPreviaExistenteException("La asignatura previa ya se encuentra registrada");
        }
        if(existeCiclo(asignatura, previa)){
            throw new CicloEnAsignaturasException("No se puede agregar la asignatura previa porque se forma un ciclo");
        }
        asignatura.getPrevias().add(previa);
        Asignatura updatedAsignatura = asignaturaRepository.save(asignatura);

        actividadService.registrarActividad(TipoActividad.REGISTRO_PREVIATURAS, "Se ha agregado la asignatura  " + previa.getNombre() + " como previa de la asignatura " + asignatura.getNombre());

        return modelMapper.map(updatedAsignatura, AsignaturaDTO.class);
    }

    private boolean existeCiclo(Asignatura asignatura, Asignatura asignaturaPrevia) {
        if(asignaturaPrevia.equals(asignatura)) {
            return true;
        }
        List<Asignatura> previas = asignaturaPrevia.getPrevias();
        if(previas == null) {
            return false;
        }
        for(Asignatura previa : previas) {
            if(existeCiclo(asignatura, previa)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AsignaturaDTO> getNoPrevias(Long asignaturaId){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + asignaturaId));

        List<Asignatura> asignaturasCarrera = asignaturaRepository.findByCarreraId(asignatura.getCarrera().getId());
        List<Asignatura> previas = asignatura.getPrevias();

        List<Asignatura> noPrevias = new ArrayList<>();
        for(Asignatura a : asignaturasCarrera){
            if(!previas.contains(a) && !a.equals(asignatura)){
                noPrevias.add(a);
            }
        }
        Type listType = new TypeToken<List<AsignaturaDTO>>(){}.getType();
        return modelMapper.map(noPrevias, listType);
    }

    @Override
    public AsignaturaDTO getAsignaturaById(Long id) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura not found with id " + id));
        return modelMapper.map(asignatura, AsignaturaDTO.class);
    }

    @Override
    public Page<ExamenDTO> obtenerExamenes(Long asignaturaId, Pageable pageable){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada - id " + asignaturaId));
        List<Examen> examenes = examenRepository.findByAsignaturaId(asignaturaId);
        if(examenes.isEmpty()){
            throw new ResourceNotFoundException("No se encontraron examenes para la asignatura con id " + asignaturaId);
        }
        List<ExamenDTO> examenesDto = examenes.stream().map(examen -> modelMapper.map(examen, ExamenDTO.class)).collect(Collectors.toList());
        return new PageImpl<>(examenesDto, pageable, examenes.size());
    }

    @Override
    public Page<ExamenDTO> obtenerExamenesEnFechaInscripcion(Long asignaturaId, Pageable pageable) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada - id " + asignaturaId));
        List<Examen> examenes = examenRepository.findByAsignaturaId(asignaturaId);
        if (examenes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron examenes para la asignatura con id " + asignaturaId);
        }
        LocalDateTime fechaActual = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Examen> examenesFiltrados = examenes.stream()
                .filter(examen -> {
                    LocalDateTime fecha = examen.getFecha();
                    LocalDateTime fechaInscripcion = fecha.minusDays(examen.getDiasPrevInsc()).withHour(0).withMinute(0).withSecond(0).withNano(0);
                    return !fechaActual.isBefore(fechaInscripcion) && fechaActual.isBefore(fecha);
                })
                .collect(Collectors.toList());
        if (examenesFiltrados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron examenes en fecha de inscripcion para la asignatura con id " + asignaturaId);
        }
        List<ExamenDTO> examenesDto = examenesFiltrados.stream().map(examen -> modelMapper.map(examen, ExamenDTO.class)).collect(Collectors.toList());
        return new PageImpl<>(examenesDto, pageable, examenesFiltrados.size());
    }

    @Override
    public Page<ExamenDTO> obtenerExamenesFueraInscripcionSinCalificar(Long asignaturaId, Pageable pageable){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada - id " + asignaturaId));
        Page<Examen> examenes = examenRepository.findAllByFechaAndEstadoAndAsignaturaId(LocalDateTime.now(), Estado.ACTIVO, asignaturaId, pageable);
        Page<Examen> examenesViejosSinCalificar = examenRepository.findAllByFechaBeforeAndEstadoAndAsignaturaId(LocalDateTime.now(), Estado.ACTIVO, asignaturaId, pageable);
        if(examenes.isEmpty() && examenesViejosSinCalificar.isEmpty()){
            throw new ResourceNotFoundException("No se encontraron examenes");
        }
        List<Examen> examenesSinCalificar = new ArrayList<>();
        examenesSinCalificar.addAll(examenes.getContent());
        examenesSinCalificar.addAll(examenesViejosSinCalificar.getContent());
        List<ExamenDTO> examenesDto = examenesSinCalificar.stream().map(examen -> modelMapper.map(examen, ExamenDTO.class)).collect(Collectors.toList());
        return new PageImpl<>(examenesDto, pageable, examenesSinCalificar.size());
    }

    @Override
    public List<CursoDTO> obtenerCursosDeAsignatura(Long asignaturaId){
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada - id " + asignaturaId));

        List<Curso> cursos = asignatura.getCursos();

        Type listType = new TypeToken<List<CursoDTO>>(){}.getType();
        return modelMapper.map(cursos, listType);
    }

    @Override
    public List<CursoDTO> obtenerCursosDeAsignaturaValidos(Long asignaturaId, String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada - id " + asignaturaId));

        List<Curso> cursos = asignatura.getCursos();

        LocalDate fechaActual = LocalDate.now();

        List<Curso> cursosValidos = cursos.stream()
                .filter(curso -> curso.getEstado() == Estado.ACTIVO)
                .filter(curso -> {
                    LocalDate fechaInicioCurso = curso.getFechaInicio();
                    int plazoDiasPrevios = curso.getDiasPrevInsc();
                    LocalDate limiteFechaInicio = fechaInicioCurso.minusDays(plazoDiasPrevios);
                    return fechaActual.isAfter(limiteFechaInicio) && fechaActual.isBefore(fechaInicioCurso);
                })
                .filter(curso -> {
                    Optional<InscripcionCurso> inscripcion = inscripcionCursoRepository.findInscripcionCursoEstudianteByEstudianteIdAndCursoId(usuario.get().getId(), curso.getId());
                    return inscripcion.isEmpty();
                })
                .collect(Collectors.toList());

        Type listType = new TypeToken<List<CursoDTO>>() {}.getType();
        return modelMapper.map(cursosValidos, listType);
    }


    @Override
    public List<CursoDTO> obtenerCursosCalificadosDeAsignatura(Long asignaturaId){
        List<Curso> cursos = cursoRepository.findByAsignaturaIdAndEstado(asignaturaId, Estado.FINALIZADO);
        return cursos.stream()
                .map(curso -> modelMapper.map(curso, CursoDTO.class))
                .toList();
    }

    @Override
    public List<ExamenDTO> obtenerExamenesCalificadosDeAsignatura(Long asignaturaId){
        List<Examen> examenes = examenRepository.findByAsignaturaIdAndEstado(asignaturaId, Estado.FINALIZADO);
        return examenes.stream()
                .map(examen -> modelMapper.map(examen, ExamenDTO.class))
                .toList();
    }
}
