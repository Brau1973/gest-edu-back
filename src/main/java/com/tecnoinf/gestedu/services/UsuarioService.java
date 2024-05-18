package com.tecnoinf.gestedu.services;

import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
