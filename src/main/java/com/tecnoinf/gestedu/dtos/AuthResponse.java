package com.tecnoinf.gestedu.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "message", "jwt", "status"})
public record AuthResponse (String email,
                            String message,
                            String jwt,
                            boolean status){
}
