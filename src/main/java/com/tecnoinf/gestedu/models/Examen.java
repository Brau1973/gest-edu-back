package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.models.enums.Estado;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="examen")
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    private Integer diasPrevInsc;
    private Estado estado = Estado.ACTIVO;

    @ManyToOne
    private Asignatura asignatura;

    @ManyToMany
    @JoinTable(
            name = "examen_docente",
            joinColumns = @JoinColumn(name = "examen_id"),
            inverseJoinColumns = @JoinColumn(name = "docente_id")
    )
    private List<Docente> docentes = new ArrayList<>();

    @OneToMany(mappedBy = "examen")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<InscripcionExamen> inscripciones = new ArrayList<>();
}
