package com.tecnoinf.gestedu.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Estudiante extends Usuario {
    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionCarrera> inscripcionesCarreras = new ArrayList<>();

    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionExamen> inscripcionesExamenes = new ArrayList<>();

    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionCurso> inscripcionesCursos = new ArrayList<>();

    @OneToMany(mappedBy = "estudiante")
    private List<Certificado> certificados;

    @OneToMany(mappedBy = "estudiante")
    private List<Notificacion> notificaciones;

    @ElementCollection
    private List<String> tokenFirebase = new ArrayList<>();
}
