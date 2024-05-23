package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @Column(length = 2000)
    private String descripcion;
    @Transient
    private Float duracionAnios;
    @Transient
    private Integer creditos;
    @Column(columnDefinition = "boolean default false")
    private Boolean existePlanEstudio = false;
    @OneToMany(mappedBy = "carrera", fetch = FetchType.EAGER)
    private List<Asignatura> asignaturas = new ArrayList<>();

    @PostLoad
    private void calculateCreditosYDuracion() {
        if(asignaturas != null && !asignaturas.isEmpty()){
            this.creditos = asignaturas.stream().mapToInt(Asignatura::getCreditos).sum();
            this.duracionAnios =  (asignaturas.stream().mapToInt(Asignatura::getSemestrePlanEstudio).max().orElse(0) / 2.0f);
        }
    }
}
