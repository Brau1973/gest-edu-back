package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name="inscripciones_carreras")
public class InscripcionCarrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Estudiante estudiante;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;

    //TODO cuando se tenga el tema de cursos se calculara en base a los cursos aprobados y de cada uno se suma los creditos que da esa asignatura
    //TODO no se usa, no lo borro para no romper otras cosas
    @Transient
    Integer creditosObtenidos;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcionCarrera estado = EstadoInscripcionCarrera.CURSANDO;
    private LocalDate fechaInscripcion = LocalDate.now();
}
