package com.tecnoinf.gestedu.dtos.usuario;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest (@NotBlank String email,
                                @NotBlank String password){
}
