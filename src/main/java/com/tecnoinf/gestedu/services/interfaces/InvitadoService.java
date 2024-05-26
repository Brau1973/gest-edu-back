package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.EmailValuesDTO;
import com.tecnoinf.gestedu.dtos.usuario.AuthLoginRequest;
import com.tecnoinf.gestedu.dtos.usuario.AuthResponse;

public interface InvitadoService {
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest);
    public void sendEmailResetPass(EmailValuesDTO dto);
}
