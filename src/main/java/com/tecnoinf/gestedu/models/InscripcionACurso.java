package com.tecnoinf.gestedu.models;

import com.tecnoinf.gestedu.enumerados.CalificacionCursada;
import com.tecnoinf.gestedu.enumerados.EstadoInscACurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="inscripcion_Cursos")
public class InscripcionACurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private EstadoInscACurso estadoInscACurso;
    private CalificacionCursada calificacion;
    private Date fechaInscripcion;
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}
