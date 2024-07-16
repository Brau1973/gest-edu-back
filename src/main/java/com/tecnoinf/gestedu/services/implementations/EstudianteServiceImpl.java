package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.dtos.escolaridad.*;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.exceptions.UniqueFieldException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.CertificadoService;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final AsignaturaRepository asignaturaRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final InscripcionExamenRepository inscripcionExamenRepository;
    private final InscripcionCarreraRepository inscripcionCarreraRepository;
    private final CertificadoService certificadoService;

    @Autowired
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository , CarreraRepository carreraRepository,
                                 UsuarioRepository usuarioRepository, ModelMapper modelMapper, AsignaturaRepository asignaturaRepository,
                                 InscripcionCursoRepository inscripcionCursoRepository, InscripcionExamenRepository inscripcionExamenRepository,
                                 InscripcionCarreraRepository inscripcionCarreraRepository, CertificadoService certificadoService) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.asignaturaRepository = asignaturaRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.inscripcionExamenRepository = inscripcionExamenRepository;
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.certificadoService = certificadoService;
    }

    @Override
    public Page<BasicInfoUsuarioDTO> obtenerEstudiantes(Pageable pageable) {
        List<BasicInfoUsuarioDTO> estudianteList = new ArrayList<>();
        Page<Usuario> estudiantes = usuarioRepository.findAll(pageable);
        if(estudiantes != null){
            for(Usuario estudiante : estudiantes){
                if(estudiante instanceof Estudiante){
                    BasicInfoUsuarioDTO dto = new BasicInfoUsuarioDTO(estudiante);
                    estudianteList.add(dto);
                }
            }
        }
        return new PageImpl<>(estudianteList, pageable, estudianteList.size());
    }

    @Override
    public Optional<BasicInfoUsuarioDTO> obtenerEstudiantePorCi(String ci) {
        Optional<Usuario> estudiante = usuarioRepository.findByCi(ci);
        if(estudiante.isPresent() && estudiante.get() instanceof Estudiante){
            return Optional.of(new BasicInfoUsuarioDTO(estudiante.get()));
        }
        return Optional.empty();
    }

    @Override
    public Page<BasicInfoCarreraDTO> getCarrerasNoInscripto(String email, Pageable pageable) {
        Optional<Usuario> estudiante = estudianteRepository.findByEmail(email);
        if (estudiante.isEmpty()) {
            throw new ResourceNotFoundException("Estudiante con email " + email + " no encontrado");
        }
        Page<Carrera> carreras = carreraRepository.findCarrerasWithPlanEstudioAndEstudianteNotInscripto(estudiante.get().getId(), pageable);
        List<BasicInfoCarreraDTO> carreraDTOs = carreras.stream()
                .map(carrera -> modelMapper.map(carrera, BasicInfoCarreraDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(carreraDTOs, pageable, carreraDTOs.size());
    }

    @Override
    public Page<BasicInfoCarreraDTO> getCarrerasInscripto(String email, Pageable pageable){
        Optional<Usuario> estudiante = estudianteRepository.findByEmail(email);
        if (estudiante.isEmpty()) {
            throw new ResourceNotFoundException("Estudiante con email " + email + " no encontrado");
        }
        Page<Carrera> carreras = carreraRepository.findCarrerasWithPlanEstudioAndEstudianteInscripto(estudiante.get().getId(), pageable);
        List<BasicInfoCarreraDTO> carreraDTOs = carreras.stream()
                .map(carrera -> modelMapper.map(carrera, BasicInfoCarreraDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(carreraDTOs, pageable, carreraDTOs.size());
    }

    @Override
    public Page<BasicInfoCarreraDTO> getCarrerasInscriptoNoCompletadas(String email, Pageable pageable){
        Optional<Usuario> estudiante = estudianteRepository.findByEmail(email);
        if (estudiante.isEmpty()) {
            throw new ResourceNotFoundException("Estudiante con email " + email + " no encontrado");
        }
        Page<Carrera> carreras = carreraRepository.findCarrerasWithPlanEstudioAndEstudianteInscriptoAndInscripcionEstadoCursando(estudiante.get().getId(), pageable);
        List<BasicInfoCarreraDTO> carreraDTOs = carreras.stream()
                .map(carrera -> modelMapper.map(carrera, BasicInfoCarreraDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(carreraDTOs, pageable, carreraDTOs.size());
    }

    @Override
    public Page<AsignaturaDTO> obtenerAsignaturasAExamen(Long carreraId, String email, Pageable pageable){
        Optional<Usuario> estudiante = estudianteRepository.findByEmail(email);
        if (estudiante.isEmpty()) {
            throw new ResourceNotFoundException("Estudiante con email " + email + " no encontrado");
        }
        List<InscripcionCurso> inscripcionAExamen = inscripcionCursoRepository.findByCalificacionAndEstudianteId(CalificacionCurso.AEXAMEN, estudiante.get().getId());
        List<AsignaturaDTO> asignaturaDTOs = new ArrayList<>();
        for(InscripcionCurso inscripcionCurso : inscripcionAExamen){
            if(inscripcionCurso.getCurso().getAsignatura().getCarrera().getId().equals(carreraId)){
                AsignaturaDTO asignaturaDTO = modelMapper.map(inscripcionCurso.getCurso().getAsignatura(), AsignaturaDTO.class);
                asignaturaDTOs.add(asignaturaDTO);
            }
        }
        return new PageImpl<>(asignaturaDTOs, pageable, asignaturaDTOs.size());
    }

    public Page<ExamenDTO> listarExamenesInscriptoVigentes(String name, Pageable pageable){
        Optional<Usuario> estudiante = Optional.ofNullable(estudianteRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado")));
        if(!(estudiante.get() instanceof Estudiante)){
            throw new ResourceNotFoundException("El usuario no es un estudiante");
        }
        List<InscripcionExamen> inscripciones = inscripcionExamenRepository.findByEstudianteIdAndExamenFechaIsAfter(estudiante.get().getId(), LocalDateTime.now());

        List<ExamenDTO> examenes = inscripciones.stream()
                .map(inscripcion -> modelMapper.map(inscripcion.getExamen(), ExamenDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(examenes, pageable, examenes.size());
    }

    @Override
    public Page<AsignaturaDTO> obtenerAsignaturasPendientes(Long carreraId, String name, Pageable pageable){
        Optional<Usuario> estudiante = Optional.ofNullable(estudianteRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado")));
        List<Asignatura> asignaturasCarrera = new ArrayList<>(asignaturaRepository.findByCarreraId(carreraId));
        List<Asignatura> cursosAprobados = inscripcionCursoRepository.findByCalificacionAndEstudianteId(CalificacionCurso.EXONERADO, estudiante.get().getId() )
                .stream()
                .map(inscripcionCurso -> inscripcionCurso.getCurso().getAsignatura())
                .collect(Collectors.toList());
        asignaturasCarrera.removeAll(cursosAprobados);
        List<Asignatura> examenesAprobados = inscripcionExamenRepository.findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.get().getId())
                .stream()
                .map(inscripcionCurso -> inscripcionCurso.getExamen().getAsignatura())
                .collect(Collectors.toList());
        asignaturasCarrera.removeAll(examenesAprobados);
        List<AsignaturaDTO> asignaturaDTOs = asignaturasCarrera.stream()
                .map(asignatura -> modelMapper.map(asignatura, AsignaturaDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(asignaturaDTOs, pageable, asignaturaDTOs.size());
    }

    @Override
    public CertificadoDTO solicitarCertificado(Long carreraId, String email){
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        if(!(usuario.get() instanceof Estudiante estudiante)){
            throw new ResourceNotFoundException("El usuario no es un estudiante");
        }
        InscripcionCarrera inscripcionCarrera = inscripcionCarreraRepository.findByEstudianteIdAndCarreraId(estudiante.getId(), carreraId);
        if(inscripcionCarrera == null){
            throw new ResourceNotFoundException("El estudiante no esta inscripto en la carrera");
        }
        return certificadoService.generarCertificado(inscripcionCarrera.getCarrera().getNombre(), estudiante);
    }

    public boolean isAsignaturaExoneradaPorEstudiante(Estudiante estudiante, Asignatura asignatura) {
        List<Curso> cursos = asignatura.getCursos();
        for (Curso curso : cursos) {
            List<InscripcionCurso> inscripcionesCurso = inscripcionCursoRepository.findInscripcionCursoEstudianteById(estudiante.getId());
            for (InscripcionCurso inscripcion : inscripcionesCurso) {
                if (inscripcion.getCurso().equals(curso)) {
                    if (inscripcion.getCalificacion().equals(CalificacionCurso.EXONERADO)) {
                        return true;
                    }
                    if (inscripcion.getCalificacion().equals(CalificacionCurso.AEXAMEN)) {
                        List<InscripcionExamen> inscripcionesExamen = inscripcionExamenRepository.findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId());
                        for (InscripcionExamen inscripcionExamen : inscripcionesExamen) {
                            if (curso.getAsignatura().getExamenes().contains(inscripcionExamen.getExamen())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Page<AsignaturaDTO> obtenerAsignaturasParaInscripcion(Long id, String emailEstudiante, Pageable pageable){
        Optional<Usuario> usuario = usuarioRepository.findByEmail(emailEstudiante);
        if(usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        if(!(usuario.get() instanceof Estudiante estudiante)){
            throw new ResourceNotFoundException("El usuario no es un estudiante");
        }
        List<AsignaturaDTO> asignaturaDTOS = new ArrayList<>();
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera not found with id " + id));
        List<Asignatura> asignaturas = carrera.getAsignaturas();
        if (asignaturas != null){
            boolean periodoInscripcion = false;
            for(Asignatura auxAsignatura: asignaturas){
                // Verificar si el estudiante ya exoneró la asignatura
                if(isAsignaturaExoneradaPorEstudiante(estudiante, auxAsignatura)){
                    continue; // Saltar esta asignatura
                }
                if(auxAsignatura.getSemestrePlanEstudio() == 1){
                    //No tiene previas...
                    List<Curso> cursos = auxAsignatura.getCursos();
                    if(cursos != null) {
                        for(Curso auxCursos: cursos){
                            LocalDate fechaActual = LocalDate.now();
                            LocalDate fechaInicioCurso = auxCursos.getFechaInicio();
                            int plazoDiasPrevios = auxCursos.getDiasPrevInsc();
                            LocalDate limiteFechaInicio = fechaInicioCurso.minusDays(plazoDiasPrevios);

                            if (fechaActual.isBefore(fechaInicioCurso)) {
                                if (fechaActual.isAfter(limiteFechaInicio)) {
                                    //Dentro del horario de Inscripcion
                                    periodoInscripcion = true;
                                    List<InscripcionCurso> inscripcionCursos = auxCursos.getInscripciones();
                                    if(inscripcionCursos == null){
                                        AsignaturaDTO asignaturaDTO = modelMapper.map(auxAsignatura, AsignaturaDTO.class);
                                        asignaturaDTOS.add(asignaturaDTO);
                                    }
                                    boolean estaInscripto = false;
                                    for(InscripcionCurso auxInscripcionCurso: inscripcionCursos){
                                        if(auxInscripcionCurso.getEstudiante().getId() == estudiante.getId()){
                                            estaInscripto = true;
                                        }
                                    }
                                    if(!estaInscripto){
                                        AsignaturaDTO asignaturaDTO = modelMapper.map(auxAsignatura, AsignaturaDTO.class);
                                        asignaturaDTOS.add(asignaturaDTO);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    List<Asignatura> previas = asignaturaRepository.findPreviasByAsignaturaId(auxAsignatura.getId());
                    int cantPrevias = 0;
                    int previasExoneradas = 0;
                    for(Asignatura previa: previas){
                        cantPrevias++;
                        List<Curso> cursosPrevia = previa.getCursos();
                        for (Curso auxCursoPrevias: cursosPrevia){
                            List<InscripcionCurso> inscripcionCursosEstudiante = inscripcionCursoRepository.findInscripcionCursoEstudianteById(estudiante.getId());
                            for(InscripcionCurso inscripcionCurso: inscripcionCursosEstudiante){
                                if(inscripcionCurso.getCurso().equals(auxCursoPrevias)){
                                    if(inscripcionCurso.getCalificacion().equals(CalificacionCurso.EXONERADO)){
                                        previasExoneradas++;
                                    } else if (inscripcionCurso.getCalificacion().equals(CalificacionCurso.AEXAMEN)){
                                        List<InscripcionExamen> examenesAprovadosEstudiante = inscripcionExamenRepository.findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId());
                                        List<Examen> examenesAsignaturas = previa.getExamenes();
                                        for(InscripcionExamen examenAprovado: examenesAprovadosEstudiante){
                                            for(Examen examenAsig: examenesAsignaturas){
                                                if(examenAprovado.getExamen().equals(examenAsig)){
                                                    previasExoneradas++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(cantPrevias == previasExoneradas){
                        List<Curso> cursos = auxAsignatura.getCursos();
                        if(cursos != null) {
                            for(Curso auxCursos: cursos){
                                LocalDate fechaActual = LocalDate.now();
                                LocalDate fechaInicioCurso = auxCursos.getFechaInicio();
                                int plazoDiasPrevios = auxCursos.getDiasPrevInsc();
                                LocalDate limiteFechaInicio = fechaInicioCurso.minusDays(plazoDiasPrevios);

                                if (fechaActual.isBefore(fechaInicioCurso)) {
                                    if (fechaActual.isAfter(limiteFechaInicio)) {
                                        periodoInscripcion = true;
                                        List<InscripcionCurso> inscripcionCursos = auxCursos.getInscripciones();
                                        if(inscripcionCursos == null){
                                            AsignaturaDTO asignaturaDTO = modelMapper.map(auxAsignatura, AsignaturaDTO.class);
                                            asignaturaDTOS.add(asignaturaDTO);
                                        }
                                        boolean estaInscripto = false;
                                        for(InscripcionCurso auxInscripcionCurso: inscripcionCursos){
                                            if(auxInscripcionCurso.getEstudiante().getId() == estudiante.getId()){
                                                estaInscripto = true;
                                            }
                                        }
                                        if(!estaInscripto){
                                            AsignaturaDTO asignaturaDTO = modelMapper.map(auxAsignatura, AsignaturaDTO.class);
                                            asignaturaDTOS.add(asignaturaDTO);
                                        }
                                    }
                                }
                            }
                        } else {
                            throw new UniqueFieldException("No existen cursos asociados a la asignatura.");
                        }
                    }
                }
            }
            if(!periodoInscripcion){
                throw new UniqueFieldException("Está fuera del plazo de inscripción de cualquier curso.");
            }
        } else {
            throw new UniqueFieldException("No existen asignaturas asociadas a la carrera.");
        }
        return new PageImpl<>(asignaturaDTOS, pageable, asignaturaDTOS.size());
    }

    @Override
    public EscolaridadDTO generarEscolaridad(Long carreraId, String name){
        EscolaridadDTO escolaridadDTO = new EscolaridadDTO();
        Optional<Usuario> usuario = usuarioRepository.findByEmail(name);
        if(!usuario.isPresent()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        if(!(usuario.get() instanceof Estudiante estudiante)){
            throw new ResourceNotFoundException("El usuario no es un estudiante");
        }
        InscripcionCarrera inscripcionCarrera = inscripcionCarreraRepository.findByEstudianteIdAndCarreraId(estudiante.getId(), carreraId);
        if(inscripcionCarrera == null){
            throw new ResourceNotFoundException("El estudiante no esta inscripto en la carrera");
        }
        escolaridadDTO.setEstudiante(new BasicInfoUsuarioDTO(usuario.get()));
        escolaridadDTO.setCarrera(modelMapper.map(inscripcionCarrera.getCarrera(), BasicInfoCarreraDTO.class));
        Integer creditosAprobados = obtenerCreditosAprobados(estudiante, inscripcionCarrera.getCarrera());
        escolaridadDTO.setCreditosAprobados(creditosAprobados);
        List<EscolaridadSemestreDTO> semestres = obtenerInfoSemestres(estudiante, inscripcionCarrera.getCarrera());
        escolaridadDTO.setSemestres(semestres);
        return escolaridadDTO;
    }

    @Override
    public Integer obtenerCreditosAprobados(Estudiante estudiante, Carrera carrera){
        Integer creditosAprobados = 0;
        Set<Asignatura> asignaturasAprobadas = new HashSet<>();

        List<InscripcionCurso> cursosAprobados = inscripcionCursoRepository.findByCalificacionAndEstudianteId(CalificacionCurso.EXONERADO, estudiante.getId());
        for(InscripcionCurso inscripcionCurso: cursosAprobados){
            if(inscripcionCurso.getCurso().getAsignatura().getCarrera().getId().equals(carrera.getId())){
                asignaturasAprobadas.add(inscripcionCurso.getCurso().getAsignatura());
            }
        }

        List<InscripcionExamen> examenesAprobados = inscripcionExamenRepository.findByCalificacionAndEstudianteId(CalificacionExamen.APROBADO, estudiante.getId());
        for(InscripcionExamen inscripcion : examenesAprobados){
            if(inscripcion.getExamen().getAsignatura().getCarrera().getId().equals(carrera.getId())){
                asignaturasAprobadas.add(inscripcion.getExamen().getAsignatura());
            }
        }

        for(Asignatura asignatura: asignaturasAprobadas){
            creditosAprobados += asignatura.getCreditos();
        }

        return creditosAprobados;
    }

    private List<EscolaridadSemestreDTO> obtenerInfoSemestres(Estudiante estudiante, Carrera carrera) {
        List<EscolaridadSemestreDTO> semestresDTO = new ArrayList<>();

        List<Asignatura> asignaturasCarrera = asignaturaRepository.findByCarreraId(carrera.getId());
        if (asignaturasCarrera == null) {
            asignaturasCarrera = new ArrayList<>();
        }

        Set<Integer> semestres = asignaturasCarrera.stream()
                .map(Asignatura::getSemestrePlanEstudio)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (Integer semestre : semestres) {
            EscolaridadSemestreDTO semestreDTO = new EscolaridadSemestreDTO();
            int anio = (int) Math.ceil(semestre / 2.0);
            semestreDTO.setAnio(anio);
            semestreDTO.setSemestre(semestre);

            List<Asignatura> asignaturasSemestre = asignaturaRepository.findByCarreraIdAndSemestrePlanEstudio(carrera.getId(), semestre);
            if (asignaturasSemestre == null) {
                asignaturasSemestre = new ArrayList<>();
            }

            List<EscolaridadAsignaturaDTO> asignaturasDTO = new ArrayList<>();
            for (Asignatura asignatura : asignaturasSemestre) {
                EscolaridadAsignaturaDTO asignaturaDTO = new EscolaridadAsignaturaDTO();
                asignaturaDTO.setId(asignatura.getId());
                asignaturaDTO.setNombre(asignatura.getNombre());
                asignaturaDTO.setCreditos(asignatura.getCreditos());

                List<EscolaridadCursoDTO> cursos = new ArrayList<>();
                List<EscolaridadExamenDTO> examenes = new ArrayList<>();

                List<InscripcionCurso> inscripcionCursos = inscripcionCursoRepository.findByEstudianteIdAndCursoAsignaturaId(estudiante.getId(), asignatura.getId());
                if (inscripcionCursos != null) {
                    for (InscripcionCurso inscripcionCurso : inscripcionCursos) {
                        EscolaridadCursoDTO cursoDTO = new EscolaridadCursoDTO(inscripcionCurso);
                        cursos.add(cursoDTO);
                    }
                }

                List<InscripcionExamen> inscripcionExamenes = inscripcionExamenRepository.findAllByEstudianteIdAndExamenAsignaturaId(estudiante.getId(), asignatura.getId());
                if (inscripcionExamenes != null) {
                    for (InscripcionExamen inscripcionExamen : inscripcionExamenes) {
                        EscolaridadExamenDTO examenDTO = new EscolaridadExamenDTO(inscripcionExamen);
                        examenes.add(examenDTO);
                    }
                }

                asignaturaDTO.setCursos(cursos);
                asignaturaDTO.setExamenes(examenes);
                asignaturasDTO.add(asignaturaDTO);
            }
            semestreDTO.setAsignaturas(asignaturasDTO);
            semestresDTO.add(semestreDTO);
        }
        return semestresDTO;
    }

}