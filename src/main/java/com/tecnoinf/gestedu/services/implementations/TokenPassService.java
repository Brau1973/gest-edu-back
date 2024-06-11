package com.tecnoinf.gestedu.services.implementations;

import com.tecnoinf.gestedu.exceptions.TokenInactivoException;
import com.tecnoinf.gestedu.exceptions.TokenInvalidoException;
import com.tecnoinf.gestedu.exceptions.TokenVencidoException;
import com.tecnoinf.gestedu.models.TokenPass;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.TokenPassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenPassService {

    @Autowired
    TokenPassRepository tokenPassRepository;

    public String crearTokenPassword(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        LocalDateTime vencimiento = LocalDateTime.now().plusHours(2);

        TokenPass tokenPass = new TokenPass();
        tokenPass.setToken(token);
        tokenPass.setVencimiento(vencimiento);
        tokenPass.setActivo(true);
        tokenPass.setUsuario(usuario);

        tokenPassRepository.save(tokenPass);

        return token;
    }

    public Optional<Usuario> validarToken(String token) {
        Optional<TokenPass> tokenPass = tokenPassRepository.findByToken(token);
        if (!tokenPass.isPresent()) {
            throw new TokenInvalidoException("El tokenPass no es válido.");
        }
        if (!tokenPass.get().getActivo()) {
            throw new TokenInactivoException("El tokenPass no está activo");
        }
        if (tokenPass.get().getVencimiento().isBefore(LocalDateTime.now())) {
            throw new TokenVencidoException("El tokenPass ha vencido");
        }
        return Optional.ofNullable(tokenPass.get().getUsuario());
    }

    public void invalidarToken(String token) {
        Optional<TokenPass> tokenPass = tokenPassRepository.findByToken(token);
        if (tokenPass.isPresent()) {
            tokenPass.get().setActivo(false);
            tokenPassRepository.save(tokenPass.get());
        }
    }

}
