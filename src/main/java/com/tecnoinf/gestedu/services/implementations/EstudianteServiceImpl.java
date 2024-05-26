package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.CarreraRepository;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository ,CarreraRepository carreraRepository, UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
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
}
