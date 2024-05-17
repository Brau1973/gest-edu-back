package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String descripcion;
    private Integer creditos;
    private Integer semestrePlanEstudio = 0;
    @ManyToOne
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    //TODO agregar campo para mapear a la asignatura en neo4j
}
