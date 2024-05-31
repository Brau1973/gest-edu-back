package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.CalificacionCurso;
import com.tecnoinf.gestedu.models.enums.EstadoInscripcionCurso;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InscripcionCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaInscripcion;
    private EstadoInscripcionCurso estado;
    private CalificacionCurso calificacion = CalificacionCurso.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;
}
