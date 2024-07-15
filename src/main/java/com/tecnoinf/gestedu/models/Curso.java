package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.Estado;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "curso")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionCurso> inscripciones = new ArrayList<>();
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer diasPrevInsc;

    @Enumerated(EnumType.STRING)  // Mapear el enumerado como String
    @Column(name = "estado", nullable = false)
    private Estado estado;

    private Boolean horario;

    @OneToMany(mappedBy = "curso")
    private List<Horario> horarios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Docente docente;
}
