package com.tecnoinf.gestedu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class ListaNegraToken {

    @Id
    private String token;

    private Instant fechaExpiracion;
}
