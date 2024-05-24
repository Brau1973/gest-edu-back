package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="asignaturas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"nombre", "carrera_id"}))
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(length = 2000)
    private String descripcion;
    private Integer creditos;
    private Integer semestrePlanEstudio = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Carrera carrera;

    @ManyToMany
    @JoinTable(
            name = "asignatura_previas",
            joinColumns = @JoinColumn(name = "asignatura_id"),
            inverseJoinColumns = @JoinColumn(name = "previa_id")
    )
    private List<Asignatura> previas = new ArrayList<>();
}