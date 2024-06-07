package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.ActividadDTO;
import com.tecnoinf.gestedu.exceptions.ResourceNotFoundException;
import com.tecnoinf.gestedu.models.Actividad;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.TipoActividad;
import com.tecnoinf.gestedu.repositories.ActividadRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.ActividadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ActividadServiceImpl(ActividadRepository actividadRepository, ModelMapper modelMapper, UsuarioRepository usuarioRepository) {
        this.actividadRepository = actividadRepository;
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void registrarActividad(TipoActividad tipoActividad, String descripcion) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with email " + username + ". No es posible registrar la actividad realizada"));

        Actividad actividad = new Actividad();
        actividad.setTipoActividad(tipoActividad);
        actividad.setDescripcion(descripcion);
        actividad.setUsuario(usuario);
        actividadRepository.save(actividad);
    }

    @Override
    public List<ActividadDTO> getActividadByUsuarioId(Long id) {
        List<Actividad> actividades = actividadRepository.findByUsuarioId(id);
        return actividades.stream()
                .map(actividad -> modelMapper.map(actividad, ActividadDTO.class))
                .toList();
    }


}
