package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.EmailValuesDTO;
import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;
import com.tecnoinf.gestedu.models.Usuario;
import jakarta.mail.MessagingException;

public interface InvitadoService {
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest);
    public void sendEmailResetPass(EmailValuesDTO dto, Usuario usuario) throws MessagingException;
}
