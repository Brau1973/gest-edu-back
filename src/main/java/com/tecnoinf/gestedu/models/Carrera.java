package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="carreras")
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private String descripcion;
    private Integer duracionAnios;
    private Integer creditos;
    @Column(columnDefinition = "boolean default false")
    private Boolean existePlanEstudio;
    @OneToMany(mappedBy = "carrera")
    private List<Asignatura> asignaturas;
}
