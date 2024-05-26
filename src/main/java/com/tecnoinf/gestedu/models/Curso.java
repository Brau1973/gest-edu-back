package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.enumerados.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer diasPrevInsc;
    @Enumerated(EnumType.STRING)  // Mapear el enumerado como String
    @Column(name = "estado", nullable = false)
    private Estado estado;
    @ManyToMany(fetch = FetchType.LAZY)
    //@JoinColumn(name = "docente_id", nullable = false)
    @JoinTable(
            name = "asignatura_docente",
            joinColumns = @JoinColumn(name = "asignatura_id"),
            inverseJoinColumns = @JoinColumn(name = "docente_id")
    )
    private Set<Docente> docentes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "curso_horario",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "horario_id")
    )
    private Set<Horario> horarios = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignaturas;

    public void addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.getCursos().add(this);
    }
}
