package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCarrera;
import jakarta.persistence.*;
import lombok.*;

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

    @Transient
    Integer creditosObtenidos;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcionCarrera estado = EstadoInscripcionCarrera.CURSANDO;
    private LocalDateTime fechaInscripcion = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
}
