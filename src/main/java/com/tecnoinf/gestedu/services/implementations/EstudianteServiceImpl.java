package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.EstudianteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

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
