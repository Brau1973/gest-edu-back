package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="docentes")
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String documento;
    private String nombre;
    private String apellido;

    @ManyToMany(mappedBy = "docentes")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Examen> examenes = new ArrayList<>();

}
