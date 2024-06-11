package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.TokenPassDTO;
import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.models.Usuario;
import jakarta.mail.MessagingException;

public interface InvitadoService {
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest);
    public void sendEmailResetPass(TokenPassDTO dto, Usuario usuario) throws MessagingException;
}
