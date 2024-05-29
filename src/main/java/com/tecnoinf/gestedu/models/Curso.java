package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.Estado;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Estado estado;

    @ManyToOne
    private Asignatura asignatura;

    @OneToMany(mappedBy = "curso")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionCurso> inscripciones = new ArrayList<>();
}
