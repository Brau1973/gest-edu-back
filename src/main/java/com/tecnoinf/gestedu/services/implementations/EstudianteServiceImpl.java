package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.certificado.CertificadoDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.*;
import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.repositories.*;
import com.tecnoinf.gestedu.services.interfaces.CertificadoService;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final AsignaturaRepository asignaturaRepository;
    private final InscripcionCursoRepository inscripcionCursoRepository;
    private final InscripcionCarreraRepository inscripcionCarreraRepository;
    private final CertificadoService certificadoService;

    @Autowired
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository , CarreraRepository carreraRepository,
                                 UsuarioRepository usuarioRepository, ModelMapper modelMapper, AsignaturaRepository asignaturaRepository,
                                 InscripcionCursoRepository inscripcionCursoRepository, InscripcionCarreraRepository inscripcionCarreraRepository,
                                 CertificadoService certificadoService) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.asignaturaRepository = asignaturaRepository;
        this.inscripcionCursoRepository = inscripcionCursoRepository;
        this.inscripcionCarreraRepository = inscripcionCarreraRepository;
        this.certificadoService = certificadoService;
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
}