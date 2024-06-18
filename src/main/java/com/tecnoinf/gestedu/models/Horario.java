package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="horarios")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)  // Mapear el enumerado como String
    @Column(name = "dia", nullable = false)
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;
}
