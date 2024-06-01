package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.Administrador;
import com.tecnoinf.gestedu.models.Funcionario;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByCi(String ci) {
        return usuarioRepository.findByCi(ci);
    }

    public Optional<Usuario> getByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario updateUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public UsuarioDTO getUsuarioDTO(String email) {
        Optional<Usuario> usuario = getByEmail(email);
        if (usuario.isPresent()) {
            return new UsuarioDTO(usuario.get());
        }
        return null;
    }

    public Page<BasicInfoUsuarioDTO> getBasicInfoUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        List<BasicInfoUsuarioDTO> basicInfoUsuarios = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            basicInfoUsuarios.add(new BasicInfoUsuarioDTO(usuario));
        }
        return new PageImpl<>(basicInfoUsuarios, pageable, usuarios.getTotalElements());
    }

    public Optional<BasicInfoUsuarioDTO> obtenerUsuarioPorCi(String ci) {
        Optional<Usuario> usuario = usuarioRepository.findByCi(ci);
        if(usuario.isPresent()){
            return Optional.of(new BasicInfoUsuarioDTO(usuario.get()));
        }
        return Optional.empty();
    }

    public Object desactivarCuentaUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()){
            Usuario user = usuario.get();
            if (user instanceof Funcionario || user instanceof Administrador) {
                user.setActivo(false);
                usuarioRepository.save(user);
                return new BasicInfoUsuarioDTO(user);
            }else{
                return "El usuario seleccionado no es coordinador o funcionario.";
            }
        }
        return null;
    }
}
