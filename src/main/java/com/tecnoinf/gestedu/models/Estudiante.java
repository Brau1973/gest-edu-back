package com.tecnoinf.gestedu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Estudiante extends Usuario {
    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionCarrera> inscripcionesCarreras = new ArrayList<>();
}
