package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.CalificacionExamen;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name="inscripciones_examenes")
public class InscripcionExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaInscripcion = LocalDateTime.now();
    private CalificacionExamen calificacion = CalificacionExamen.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "examen_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Examen examen;


}
